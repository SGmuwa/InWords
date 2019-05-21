package ru.inwords.inwords.data.repository.profile

import io.reactivex.Observable
import io.reactivex.Single
import ru.inwords.inwords.data.dto.User
import ru.inwords.inwords.data.repository.game.ResourceCachingProvider
import ru.inwords.inwords.domain.model.Resource

class UserCachingRepository(
        private val databaseRepository: UserDatabaseRepository,
        private val remoteRepository: UserRemoteRepository) : UserRepository {

    private val authorisedUserCachingProvider by lazy { createAuthorisedUserCachingProvider() }

    override fun getAuthorisedUser(forceUpdate: Boolean): Observable<Resource<User>> {
        if (forceUpdate) {
            authorisedUserCachingProvider.askForContentUpdate()
        }

        return authorisedUserCachingProvider.observe()
    }

    override fun getUserById(id: Int): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createAuthorisedUserCachingProvider(): ResourceCachingProvider<User> {
        return ResourceCachingProvider(
                { data -> databaseRepository.insert(data).map { data } },
                { databaseRepository.getAuthorisedUser() },
                { remoteRepository.getAuthorisedUser() })
    }
}