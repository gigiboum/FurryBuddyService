package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.Advertisement;
import ch.unil.furrybuddy.domain.ApplicationState;
import ch.unil.furrybuddy.domain.Pet;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/advertisements")
public class AdvertisementRessource {

    @Inject
    private ApplicationState state;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pet> getAllAdvertisements(){
        return new ArrayList<>(state.getAllPets().values());
    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{id}")
//    public Pet getAdvertisement(@PathParam("id") UUID advertisementID){
//        return state.getAdvertisement(advertisementID);
//    }
//
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/{id}")
//    public boolean setPet(@PathParam("id") UUID petID, Pet pet){
//        return state.setPet(petID, pet);
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Pet addPet(Pet pet){
//        state.addPet(pet);
//        return pet;
//    }

//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{id}")
//    public boolean deletePet(@PathParam("id") UUID petID){
//        return state.removePet(petID);
//    }


}
