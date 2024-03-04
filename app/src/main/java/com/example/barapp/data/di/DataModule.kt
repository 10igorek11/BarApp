package com.example.barapp.data.di

import com.example.barapp.data.*
import com.example.barapp.data.PassingStatusRepositoryImpl
import com.example.barapp.data.TechCardRepositoryImpl
import com.example.barapp.domain.repositories.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    fun bindTestRepository(
        testRepository: TestRepositoryImpl
    ): TestRepository

    @Binds
    @Singleton
    fun bindTechCardRepository(
        techCardRepository: TechCardRepositoryImpl
    ): TechCardRepository

    @Binds
    @Singleton
    fun bindTechCardRecipeRepository(
        techCardRecipeRepository: TechCardRecipeRepositoryImpl
    ): TechCardRecipeRepository

    @Binds
    @Singleton
    fun bindPassingStatusRepository(
        techCardRepository: PassingStatusRepositoryImpl
    ): PassingStatusRepository

    @Binds
    @Singleton
    fun bindLearningMaterialRepository(
        learningMaterialRepository: LearningMaterialRepositoryImpl
    ): LearningMaterialRepository

    @Binds
    @Singleton
    fun bindCocktailTypeRepository(
        cocktailTypeRepository: CocktailTypeRepositoryImpl
    ): CocktailTypeRepository

    @Binds
    @Singleton
    fun bindRoleRepository(
        roleRepository: RoleRepositoryImpl
    ): RoleRepository

    @Binds
    @Singleton
    fun bindPhotoRepository(
        photoRepository: PhotoRepositoryImpl
    ): PhotoRepository
}