package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.AdoptionRequest;
import ch.unil.furrybuddy.domain.ApplicationState;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/adoptionRequests")
public class AdoptionRequestRessource {
    @Inject
    private ApplicationState state;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdoptionRequest> getAllAdoptionRequests(){
        return new ArrayList<>(state.getAllAdoptionRequests().values());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public AdoptionRequest getAdoptionRequest(@PathParam("id") UUID adoptionRequestID){
        return state.getAdoptionRequest(adoptionRequestID);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean setAdoptionRequest(@PathParam("id") UUID adoptionRequestID, AdoptionRequest adoptionRequest){
        return state.setAdoptionRequest(adoptionRequestID, adoptionRequest);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AdoptionRequest addAdoptionRequest(AdoptionRequest adoptionRequest){
        state.addAdoptionRequest(adoptionRequest);
        return adoptionRequest;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean removeAdoptionRequest(@PathParam("id") UUID adoptionRequestID){
        return state.removeAdoptionRequest(adoptionRequestID);
    }
}
