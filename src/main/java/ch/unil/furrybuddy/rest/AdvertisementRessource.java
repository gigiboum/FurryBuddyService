package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.Adopter;
import ch.unil.furrybuddy.domain.Advertisement;
import ch.unil.furrybuddy.domain.ApplicationState;
import ch.unil.furrybuddy.domain.Pet;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/advertisements")
public class AdvertisementRessource {

    @Inject
    private ApplicationState state;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Advertisement> getAllAdvertisements(){
        return new ArrayList<>(state.getAllAds().values());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Advertisement getAdvertisement(@PathParam("id") UUID advertisementID){
        return state.getAdvertisement(advertisementID);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean setAdvertisement(@PathParam("id") UUID advertisementID, Advertisement advertisement){
        return state.setAdvertisement(advertisementID, advertisement);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Advertisement addAdvertisement(Advertisement advertisement){
        state.addAdvertisement(advertisement);
        return advertisement;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean removeAdvertisement(@PathParam("id") UUID advertisementID){
        return state.removeAdvertisement(advertisementID);
    }

}
