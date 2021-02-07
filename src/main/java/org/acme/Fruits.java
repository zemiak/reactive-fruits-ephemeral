package org.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.smallrye.mutiny.Uni;

@Path("/fruits")
@ApplicationScoped
public class Fruits {
    private List<Fruit> repository = new ArrayList<>();

    @GET
    public Uni<List<Fruit>> all() {
        return Uni.createFrom().item(repository);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> create(Fruit fruit) {
        return Uni.createFrom().emitter(em -> {
            fruit.id = ThreadLocalRandom.current().nextLong();
            repository.add(fruit);
            em.complete(fruit);
        }).onItem().transform(item -> Response.status(Status.CREATED).entity(item).build());
    }

    @GET
    @Path("count")
    public Uni<Integer> count() {
        return Uni.createFrom().item(repository.size());
    }
}
