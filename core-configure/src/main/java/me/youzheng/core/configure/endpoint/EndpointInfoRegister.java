package me.youzheng.core.configure.endpoint;

import me.youzheng.core.domain.endpoint.entity.Endpoint;

import java.util.List;

public interface EndpointInfoRegister {

    List<Endpoint> register(final List<Endpoint> endpoints);

}