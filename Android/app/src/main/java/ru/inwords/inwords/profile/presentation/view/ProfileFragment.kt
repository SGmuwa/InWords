package ru.inwords.inwords.profile.presentation.view

import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import ru.inwords.inwords.R
import ru.inwords.inwords.core.resource.Resource
import ru.inwords.inwords.core.rxjava.SchedulersFacade
import ru.inwords.inwords.core.utils.KeyboardUtils
import ru.inwords.inwords.core.utils.observe
import ru.inwords.inwords.databinding.FragmentProfileBinding
import ru.inwords.inwords.policy.presentation.renderPolicyText
import ru.inwords.inwords.presentation.createAppBarNavIconOnScrimColorAnimatorListener
import ru.inwords.inwords.presentation.view_scenario.FragmentWithViewModelAndNav
import ru.inwords.inwords.profile.data.bean.User
import ru.inwords.inwords.profile.presentation.ProfileViewModelFactory


class ProfileFragment : FragmentWithViewModelAndNav<ProfileViewModel, ProfileViewModelFactory, FragmentProfileBinding>() {
    override val layout = R.layout.fragment_profile
    override val classType = ProfileViewModel::class.java

    override fun bindingInflate(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, attachToRoot)
    }

    lateinit var user: User

    private var navIconAnimationListener: AppBarLayout.OnOffsetChangedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.headerBackground.hierarchy.setActualImageFocusPoint(PointF(0.5f, 0.75f))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorCollapsed = ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme)
        val colorExpanded = ResourcesCompat.getColor(resources, R.color.white, context?.theme)

        navIconAnimationListener = createAppBarNavIconOnScrimColorAnimatorListener(binding.toolbar, binding.collapsingToolbar, colorExpanded, colorCollapsed)
            .apply {
                binding.appbarLayout.addOnOffsetChangedListener(this)
            }

        setupWithNavController(binding.collapsingToolbar, binding.toolbar)

        binding.toLoginButton.setOnClickListener { viewModel.onNavigateToLoginClicked() }

        binding.settingsButton.setOnClickListener { viewModel.onNavigateToSettingsClicked() }

        binding.nameEditEditButton.setOnClickListener(nameEditClickListener())
        binding.nameEditApplyButton.setOnClickListener(nameApplyClickListener())

        subscribeUser().disposeOnViewDestroyed()

        subscribeNicknameUpdateResult()

        renderPolicyText(binding.policyTextView)
    }

    override fun onDestroyView() {
        navIconAnimationListener?.let { binding.appbarLayout.removeOnOffsetChangedListener(it) }
        super.onDestroyView()
    }

    private fun subscribeUser(): Disposable {
        fun renderUser(userResource: Resource<User>) {
            if (userResource is Resource.Success) {
                user = userResource.data

                if (user.avatar != null) {
                    val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(user.avatar))
                        .setProgressiveRenderingEnabled(true)
                        .setLocalThumbnailPreviewsEnabled(true)
                        .build()

                    binding.avatarImage.setImageRequest(request)
                } else {
                    binding.avatarImage.setActualImageResource(R.drawable.ic_octopus1)
                }

                binding.nameEditText.setText(user.userName)

                renderDefaultViewState()
                binding.nameEditEditButton.isEnabled = true
            } else {
                binding.avatarImage.setActualImageResource(R.drawable.ic_octopus1)

                renderDefaultViewState()
                binding.nameEditEditButton.isEnabled = false
            }
        }

        return viewModel.profileDataSubject
            .observeOn(SchedulersFacade.ui())
            .subscribe(::renderUser)
    }

    private fun subscribeNicknameUpdateResult() {
        observe(viewModel.changeNickname) {
            when (it.contentIfNotHandled) {
                is Resource.Success -> {
                    renderDefaultViewState()
                }
                is Resource.Loading -> {
                    renderLoadingViewState()
                }
                is Resource.Error -> { //TODO error handling
                    renderErrorViewState()

                    Snackbar.make(binding.profileRoot, "Не удалось изменить ник. Попробуйте ещё раз", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun renderDefaultViewState() {
        binding.nameEditProgress.visibility = View.INVISIBLE
        binding.nameEditEditButton.visibility = View.VISIBLE
        binding.nameEditApplyButton.visibility = View.INVISIBLE
        binding.nameEditText.isEnabled = false
    }

    private fun renderEditViewState() {
        binding.nameEditProgress.visibility = View.INVISIBLE
        binding.nameEditEditButton.visibility = View.INVISIBLE
        binding.nameEditApplyButton.visibility = View.VISIBLE
        binding.nameEditText.isEnabled = true
    }

    private fun renderLoadingViewState() {
        binding.nameEditProgress.visibility = View.VISIBLE
        binding.nameEditEditButton.visibility = View.INVISIBLE
        binding.nameEditApplyButton.visibility = View.INVISIBLE
        binding.nameEditText.isEnabled = false
    }

    private fun renderErrorViewState() {
        binding.nameEditProgress.visibility = View.INVISIBLE
        binding.nameEditEditButton.visibility = View.INVISIBLE
        binding.nameEditApplyButton.visibility = View.VISIBLE
        binding.nameEditText.isEnabled = true
    }

    private fun nameApplyClickListener() = View.OnClickListener {
        if (::user.isInitialized) {
            val newUsername = binding.nameEditText.text.toString()

            if (newUsername != user.userName) { //TODO validate also
                val newUser = user.copy(userName = newUsername, account = null)

                viewModel.updateUser(newUser)

                renderLoadingViewState()
            } else {
                renderDefaultViewState()
            }
        }
    }

    private fun nameEditClickListener() = View.OnClickListener {
        renderEditViewState()
        KeyboardUtils.showKeyboard(binding.nameEditText)
        binding.nameEditText.setSelection(binding.nameEditText.text?.length ?: 0)
    }

}
