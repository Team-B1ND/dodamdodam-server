package com.b1nd.dodamdodam.grpc.exception.handler

import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.b1nd.dodamdodam.grpc.exception.mapper.httpStatusToGrpcStatus
import io.grpc.ForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status

class GrpcExceptionHandler: ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val delegate = next.startCall(call, headers)

        return object: ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
            override fun onHalfClose() {
                try {
                    super.onHalfClose()
                } catch (e: BasicException) {
                    handleException(e, call)
                } catch (e: Error) {
                    call.close(Status.INTERNAL, Metadata())
                }
            }
        }
    }

    private fun <ReqT, RespT> handleException(
        exception: BasicException,
        call: ServerCall<ReqT, RespT>
    ) {
        val status = httpStatusToGrpcStatus(exception.exceptionCode.status)
        call.close(status, Metadata())
    }
}