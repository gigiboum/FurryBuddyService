package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.ApplicationState;
import ch.unil.furrybuddy.domain.PetOwner;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/petOwners")
public class PetOwnerRessource {
    @Inject
    private ApplicationState state;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PetOwner> getAllPetOwners(){
        return new ArrayList<>(state.getAllPetOwners().values());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public PetOwner getPetOwner(@PathParam("id") UUID petOwnerID){
        return state.getPetOwner(petOwnerID);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean setPetOwner(@PathParam("id") UUID petOwnerID, PetOwner petOwner){
        return state.setPetOwner(petOwnerID, petOwner);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PetOwner addPetOwner(PetOwner petOwner){
        state.addPetOwner(petOwner);
        return petOwner;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean deletePetOwner(@PathParam("id") UUID petOwnerID){
        return state.removePetOwner(petOwnerID);
    }
}
