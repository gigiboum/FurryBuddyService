package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.AdoptionRequest;
import ch.unil.furrybuddy.domain.Advertisement;
import ch.unil.furrybuddy.domain.ApplicationState;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.UUID;

@Path("/service")
public class ServiceResource {
    @Inject
    private ApplicationState state;

    // RESET SERVICE
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/reset")
    public Response reset() {
        state.init();
        return Response.ok("Furry buddy Service was reset at " + LocalDateTime.now()).build();
    }

    // CREATE AD
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/createAdvertisement")
    public Advertisement createAdvertisement(Advertisement advertisement, @PathParam("petOwner") UUID petOwnerID) {
        var pet = state.getPet(advertisement.getPet().getPetID());
        state.getPetOwner(petOwnerID).createAdvertisement(pet);
        state.addAdvertisement(advertisement);
        return advertisement;
    }

    // DELETE AD
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/deleteAdvertisement/{adID}")
    public boolean deleteAdvertisement(@PathParam("petOwner") UUID petOwnerID, @PathParam("adID") UUID advertisementID) {
        var ad = state.getAdvertisement(advertisementID);
        state.getPetOwner(petOwnerID).deleteAdvertisement(ad);
        return state.removeAdvertisement(advertisementID);
    }

    //CREATE ADOPTION REQUEST
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{adopter}/createAdoptionRequest/{adID}")
    public AdoptionRequest createAdoptionRequest(AdoptionRequest adoptionRequest, @PathParam("adID") UUID advertisementID, @PathParam("adopter") UUID adopterID) {
        var advertisement = state.getAdvertisement(advertisementID);

        state.getAdopter(adopterID).createAdoptionRequest(advertisement);
        state.addAdoptionRequest(adoptionRequest);
        return adoptionRequest;
    }

    // CANCEL ADOPTION REQUEST
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{adopter}/cancelAdoptionRequest/{adID}")
    public AdoptionRequest deleteAdoptionRequest(@PathParam("adopter") UUID adopterID, @PathParam("adID") UUID adoptionRequestID) {
        var adoptionRequest = state.getAdoptionRequest(adoptionRequestID);
        state.getAdopter(adopterID).cancelAdoptionRequest(adoptionRequest);
        return adoptionRequest;
    }

    //ACCEPT AR
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/acceptAdoptionRequest/{adoptionReqID}")
    public boolean acceptRequest(@PathParam("petOwner") UUID petOwnerID, @PathParam("adoptionReqID") UUID adoptionRequestID) {
        var request = state.getAdoptionRequest(adoptionRequestID);
        state.getPetOwner(petOwnerID).acceptRequest(request);
        return true;
    }

    //ACCEPT AR
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/rejectAdoptionRequest/{adoptionReqID}")
    public boolean rejectAdoptionRequest(@PathParam("petOwner") UUID petOwnerID, @PathParam("adoptionReqID") UUID adoptionRequestID) {
        var request = state.getAdoptionRequest(adoptionRequestID);
        state.getPetOwner(petOwnerID).rejectRequest(request);
        return true;
    }

}