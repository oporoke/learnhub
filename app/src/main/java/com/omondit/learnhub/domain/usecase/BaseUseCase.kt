package com.omondit.learnhub.domain.usecase

/**
 * Base class for use cases that execute in background and return a result
 */
abstract class BaseUseCase<in Params, out Type> {
    abstract suspend operator fun invoke(params: Params): Result<Type>
}

/**
 * For use cases that don't need parameters
 */
abstract class NoParamsUseCase<out Type> {
    abstract suspend operator fun invoke(): Result<Type>
}