package ru.inwords.inwords.presentation.viewScenario.translation.translationMain


import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_translation_main.*
import ru.inwords.inwords.R
import ru.inwords.inwords.core.RxDiffUtil
import ru.inwords.inwords.core.util.SchedulersFacade
import ru.inwords.inwords.data.dto.WordTranslation
import ru.inwords.inwords.presentation.viewScenario.FragmentWithViewModelAndNav
import ru.inwords.inwords.presentation.viewScenario.translation.TranslationViewModelFactory
import ru.inwords.inwords.presentation.viewScenario.translation.recycler.ItemTouchHelperAdapter
import ru.inwords.inwords.presentation.viewScenario.translation.recycler.ItemTouchHelperEvents
import ru.inwords.inwords.presentation.viewScenario.translation.recycler.WordTranslationsAdapter
import ru.inwords.inwords.presentation.viewScenario.translation.recycler.WordTranslationsDiffUtilCallback

class TranslationMainFragment : FragmentWithViewModelAndNav<TranslationMainViewModel, TranslationViewModelFactory>(), ItemTouchHelperEvents {
    private lateinit var adapter: WordTranslationsAdapter

    private lateinit var mediaPlayer: MediaPlayer
    private val attrs = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer = MediaPlayer()

        val onItemClickedListener = PublishSubject.create<WordTranslation>()
        val onSpeakerClickedListener = PublishSubject.create<WordTranslation>()
        setupRecyclerView(view, onItemClickedListener, onSpeakerClickedListener)

        viewModel.addEditWordLiveData.observe(this, Observer { event ->
            if (event.handle()) {
                val args = Bundle()
                val wordTranslation = event.peekContent()
                args.putSerializable(WordTranslation::class.java.canonicalName, wordTranslation)
                navController.navigate(R.id.action_translationMainFragment_to_addEditWordFragment, args)
            }
        })

        compositeDisposable.add(viewModel.ttsStream
                .observeOn(SchedulersFacade.ui())
                .subscribe { resource ->
                    progress_view.post { progress_view.progress = 0 }

                    if (resource.success()) {
                        playAudio(resource.data!!)
                    } else {
                        Toast.makeText(context, getString(R.string.unable_to_load_voice), Toast.LENGTH_SHORT).show()
                    }
                })

        compositeDisposable.add(viewModel.translationWordsStream
                .compose(RxDiffUtil.calculate { oldItems, newItems -> WordTranslationsDiffUtilCallback.create(oldItems, newItems) })
                .observeOn(SchedulersFacade.ui())
                .subscribe(adapter))

        viewModel.onAddClickedHandler(RxView.clicks(fab))
        viewModel.onEditClickedHandler(onItemClickedListener)
        viewModel.onSpeakerClickedHandler(onSpeakerClickedListener.doOnNext { progress_view.progress = 50 })
    }

    override fun onDestroyView() {
        mediaPlayer.stop()
        mediaPlayer.release()

        super.onDestroyView()
    }

    private fun playAudio(path: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.setAudioAttributes(attrs)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun setupRecyclerView(view: View, onItemClickedListener: Subject<WordTranslation>, onSpeakerClickedListener: Subject<WordTranslation>) {
        val context = view.context

        adapter = WordTranslationsAdapter(LayoutInflater.from(context), onItemClickedListener, onSpeakerClickedListener)
        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(dividerItemDecoration)

        val callback = ItemTouchHelperAdapter(this) //TODO
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recycler_view)
    }

    override fun onItemDismiss(position: Int) {
        val item = adapter.values[position].clone()
        viewModel.onItemDismiss(item.clone())

        Snackbar.make(asdasd, getString(R.string.translation_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo_translation_deletion)) { viewModel.onItemDismissUndo(item) }
                .addCallback(SnackBarCallback())
                .show()
    }

    private inner class SnackBarCallback : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
            viewModel.onConfirmItemDismiss()
        }
    }

    override fun getLayout() = R.layout.fragment_translation_main

    override fun getClassType() = TranslationMainViewModel::class.java
}
