package ch.unil.furrybuddy.rest;

import ch.unil.furrybuddy.domain.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Path("/service")
public class ServiceResource {
    @Inject
    private ApplicationState state;

    // DB
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/populateDB")
    public Response populateDB() {
        state.populateDB();
        return Response.ok("FurryBuddy DB was populated at " + LocalDateTime.now()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/clearDB")
    public Response clearDB() {
        try {
            state.clearDB();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        state.clearDB();
        return Response.ok("FurryBuddy DB was cleared at " + LocalDateTime.now()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/resetDB")
    public Response resetDB() {
        state.resetDB();
        return Response.ok("FurryBuddy DB was reset at " + LocalDateTime.now()).build();
    }


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
        Pet pet = advertisement.getPet();

        // Check if the Pet exists
        if (pet.getPetID() == null || !state.hasPet(pet.getPetID())) {
            // Generate a new ID for the Pet
            pet.setPetID(UUID.randomUUID());
            state.addPet(pet.getPetID(), pet); // Assuming there's a method to add a Pet to the state
            System.out.println("New Pet created with ID: " + pet.getPetID());
        }

        // Create the Advertisement
        Advertisement newAd = state.getPetOwner(petOwnerID).createAdvertisement(pet);
        state.addAdvertisement(newAd);
        return newAd;
    }

    // DELETE AD
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/deleteAdvertisement/{adID}")
    public boolean deleteAdvertisement(@PathParam("petOwner") UUID petOwnerID, @PathParam("adID") UUID advertisementID) {
        var ad = state.getAdvertisement(advertisementID);
//        state.getPetOwner(petOwnerID).deleteAdvertisement(ad);
        return state.removeAdvertisement(advertisementID);
    }

    //CREATE ADOPTION REQUEST
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{adopter}/createAdoptionRequest")
    public AdoptionRequest createAdoptionRequest(AdoptionRequest adoptionRequest, @PathParam("adopter") UUID adopterID) {
        var advertisement = state.getAdvertisement(adoptionRequest.getAdvertisement().getAdvertisementID());
        AdoptionRequest newAdoptionRequest = state.getAdopter(adopterID).createAdoptionRequest(advertisement, adoptionRequest.getMessage());
        state.addAdoptionRequest(newAdoptionRequest.getRequestID(), adoptionRequest);
        state.addAdoptionRequest(newAdoptionRequest);
        return newAdoptionRequest;
    }

    // CANCEL ADOPTION REQUEST
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{adopter}/cancelAdoptionRequest/{requestID}")
    public boolean cancelAdoptionRequest(@PathParam("adopter") UUID adopterID, @PathParam("requestID") UUID adoptionRequestID) {
        var adoptionRequest = state.getAdoptionRequest(adoptionRequestID);
        AdoptionRequest cancelledAdoptionRequest = state.cancelAdoptionRequest(adoptionRequest);
        state.setAdoptionRequest(adoptionRequestID, cancelledAdoptionRequest);
        return true;
    }

    //ACCEPT AR
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/acceptAdoptionRequest/{adoptionReqID}")
    public boolean acceptRequest(@PathParam("petOwner") UUID petOwnerID, @PathParam("adoptionReqID") UUID adoptionRequestID) {
        var request = state.getAdoptionRequest(adoptionRequestID);
        AdoptionRequest acceptRequest = state.acceptAdoptionRequest(request);
        state.setAdoptionRequest(adoptionRequestID, acceptRequest);
        return true;
    }

    //REJECT AR
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{petOwner}/rejectAdoptionRequest/{adoptionReqID}")
    public boolean rejectAdoptionRequest(@PathParam("petOwner") UUID petOwnerID, @PathParam("adoptionReqID") UUID adoptionRequestID) {
        var request = state.getAdoptionRequest(adoptionRequestID);
        AdoptionRequest rejecttedAdoptionRequest = state.rejecttAdoptionRequest(request);
        state.setAdoptionRequest(adoptionRequestID, rejecttedAdoptionRequest);
        return true;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/authenticate/{username}/{password}/{role}")
    public UUID authenticate(@PathParam("username") String username, @PathParam("password") String password, @PathParam("role") String role) {
        if (role.equals("petOwner")) {
            return state.authenticate(username, password, true);
        }
        if (role.equals("adopter")) {
            return state.authenticate(username, password, false);
        }
        return null;
    }

    // FILTER THROUGH ADS
    @GET
    @Path("/advertisements/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Advertisement> filterAdvertisements(
            @QueryParam("species") String species,
            @QueryParam("breed") String breed,
            @QueryParam("gender") String gender,
            @QueryParam("compatibility") List<String> compatibility) {
        return state.filterAdvertisements(species, breed, gender, compatibility);
    }
}