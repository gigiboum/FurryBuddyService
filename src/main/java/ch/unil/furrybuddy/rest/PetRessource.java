package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/pets")
public class PetRessource {

    @Inject
    private ApplicationState state;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pet> getAllPets(){
        return new ArrayList<>(state.getAllPets().values());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Pet getPet(@PathParam("id") UUID petID){
        return state.getPet(petID);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean setPet(@PathParam("id") UUID petID, Pet pet){
        return state.setPet(petID, pet);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Pet addPet(Pet pet){
        state.addPet(pet);
        return pet;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean deletePet(@PathParam("id") UUID petID){
        return state.removePet(petID);
    }

}
