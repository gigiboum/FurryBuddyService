package furrybuddy.rest;

import Domain.Adopter;
import furrybuddy.domain.ApplicationState;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/adopters")
public class AdopterRessource {
    @Inject
    private ApplicationState state;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Adopter> getAllAdopters(){
        return new ArrayList<>(state.getAllAdopters().values());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Adopter getAdopter(@PathParam("id") UUID adopterID){
        return state.getAdopter(adopterID);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean setAdopter(@PathParam("id") UUID adopterID, Adopter adopter){
        return state.setAdopter(adopterID, adopter);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Adopter addAdopter(Adopter adopter){
        state.addAdopter(adopter);
        return adopter;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean deleteAdopter(@PathParam("id") UUID adopterID){
        return state.removeAdopter(adopterID);
    }
}
