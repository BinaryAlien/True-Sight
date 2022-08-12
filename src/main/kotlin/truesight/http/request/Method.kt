package truesight.http.request

enum class Method(val description: String) {
    GET("Requests a representation of the specified resource."),
    HEAD("Asks for a response identical to a GET request, but without the response body."),
    POST("Submits an entity to the specified resource, often causing a change in state or side effects on the server."),
    PUT("Replaces all current representations of the target resource with the request payload."),
    DELETE("Deletes the specified resource."),
    CONNECT("Establishes a tunnel to the server identified by the target resource."),
    OPTIONS("Describes the communication options for the target resource."),
    TRACE("Performs a message loop-back test along the path to the target resource."),
    PATCH("Applies partial modifications to a resource.");

    companion object {
        fun from(method: String) = Method.values().find { method == it.name } ?: GET
    }
}
