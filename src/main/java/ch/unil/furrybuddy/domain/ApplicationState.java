package ch.unil.furrybuddy.domain;

import ch.unil.furrybuddy.rest.AdoptionRequestRessource;
import ch.unil.furrybuddy.rest.AdvertisementRessource;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@ApplicationScoped
public class ApplicationState {

    private Map<UUID, Adopter> adopters;
    private Map<UUID, PetOwner> petOwners;
    private Map<UUID, Pet> pets;
    private Map<UUID, User> users;
    private Map<UUID, Advertisement> advertisements;
    private Map<UUID, AdoptionRequest> adoptionRequests;

    @PostConstruct
    public void init() {
        adopters = new TreeMap<>();
        petOwners = new TreeMap<>();
        pets = new TreeMap<>();
        users = new TreeMap<>();
        advertisements = new TreeMap<>();
        adoptionRequests = new TreeMap<>();

        populateApplicationState();
    }

    // PET
    // CREATE
    public Pet addPet(Pet pet) {
        if (pet.getPetID() != null) {
            return addPet(pet.getPetID(), pet);
        }
        return addPet(UUID.randomUUID(), pet);
    }

    public Pet addPet(UUID petID, Pet pet) {
        pet.setPetID(petID);
        pets.put(petID, pet);
        return pet;
    }

    // READ
    public Pet getPet(UUID petID) {
        return pets.get(petID);
    }

    public Map<UUID, Pet> getAllPets() {
        return pets;
    }

    // UPDATE
    public boolean setPet(UUID petID, Pet pet) {
        var thePet = pets.get(petID);
        if (thePet == null) {
            return false;
        }
        thePet.replaceWith(pet);
        return true;
    }

    // DELETE
    public boolean removePet(UUID petID) {
        var pet = pets.get(petID);
        if (pet == null) {
            return false;
        }
        pets.remove(petID);
        return true;
    }

    // PET OWNER
    // CREATE
    public PetOwner addPetOwner(PetOwner petOwner) {
        if (petOwner.getUserID() != null) {
            return addPetOwner(petOwner.getUserID(), petOwner);
        }
        return addPetOwner(UUID.randomUUID(), petOwner);
    }

    public PetOwner addPetOwner(UUID userID, PetOwner petOwner) {
//        var email = petOwner.getEmail();
//        if (email == null || email.isBlank()) {
//            throw new IllegalArgumentException("PetOwner email is null or empty");
//        }
//        if (users.containsKey(email)) {
//            throw new IllegalArgumentException("PetOwner email already exists");
//        }
//        if (petOwner.getPassword() == null || petOwner.getPassword().isBlank()) {
//            throw new IllegalArgumentException("PetOwner password is null or empty");
//        }

        petOwner.setUserID(userID);
        petOwners.put(userID, petOwner);
        users.put(userID, petOwner);
        return petOwner;
    }

    // READ
    public PetOwner getPetOwner(UUID userID) {
        return petOwners.get(userID);
    }

    public Map<UUID, PetOwner> getAllPetOwners() {
        return petOwners;
    }

    //UPDATE
    public boolean setPetOwner(UUID userID, PetOwner petOwner) {
        var thePetOwner = petOwners.get(userID);
        if (thePetOwner == null) {
            return false;
        }
        thePetOwner.replaceWith(petOwner);
        return true;
    }

    //DELETE
    public boolean removePetOwner(UUID petOwnerID) {
        var petOwner = petOwners.get(petOwnerID);
        if (petOwner == null) {
            return false;
        }
        petOwners.remove(petOwnerID);
        return true;
    }

    // ADOPTER
    // CREATE
    public Adopter addAdopter(Adopter adopter) {
        if (adopter.getUserID() != null) {
            return addAdopter(adopter.getUserID(), adopter);
        }
        return addAdopter(UUID.randomUUID(), adopter);
    }

    public Adopter addAdopter(UUID userID, Adopter adopter) {
//        var email = adopter.getEmail();
//        if (email == null || email.isBlank()) {
//            throw new IllegalArgumentException("Adopter email is null or empty");
//        }
//        if (users.containsKey(email)) { //TODO
//            throw new IllegalArgumentException("Adopter email already exists");
//        }
//        if (adopter.getPassword() == null || adopter.getPassword().isBlank()) {
//            throw new IllegalArgumentException("adopter password is null or empty");
//        }

        adopter.setUserID(userID);
        adopters.put(userID, adopter);
        users.put(userID, adopter);
        return adopter;
    }

    // READ
    public Adopter getAdopter(UUID userID) {
        return adopters.get(userID);
    }

    public Map<UUID, Adopter> getAllAdopters() {
        return adopters;
    }

    //UPDATE
    public boolean setAdopter(UUID userID, Adopter adopter) {
        var theAdopter = adopters.get(userID);
        if (theAdopter == null) {
            return false;
        }
        theAdopter.replaceWith(adopter);
        return true;
    }

    //DELETE
    public boolean removeAdopter(UUID adopterID) {
        var adopter = adopters.get(adopterID);
        if (adopter == null) {
            return false;
        }
        adopters.remove(adopterID);
        return true;
    }

    //ADVERTISEMENTS
    //CREATE
    public Advertisement addAdvertisement(Advertisement advertisement) {
        if (advertisement.getAdvertisementID() != null) {
            return addAdvertisement(advertisement.getAdvertisementID(), advertisement);
        }
        return addAdvertisement(UUID.randomUUID(), advertisement);
    }

    public Advertisement addAdvertisement(UUID advertisementID, Advertisement advertisement) {
//        var petownerads = advertisement.getPetOwner().getAdvertisements();
        advertisements.put(advertisementID, advertisement);
//        petownerads.add(advertisement);
        return advertisement;
    }

    // READ
    public Advertisement getAdvertisement(UUID advertisementID) {
        return advertisements.get(advertisementID);
    }

    public Map<UUID, Advertisement> getAllAds() {
        return advertisements;
    }

    //UPDATE
    public boolean setAdvertisement(UUID advertisementID, Advertisement advertisement) {
        var theAdvertisement = advertisements.get(advertisementID);
        if (theAdvertisement == null) {
            return false;
        }
        theAdvertisement.replaceWith(advertisement);
        return true;
    }

    //DELETE
    public boolean removeAdvertisement(UUID advertisementID) {
        var advertisement = advertisements.get(advertisementID);
        if (advertisement == null) {
            return false;
        }
        advertisements.remove(advertisementID);
        return true;
    }

    //ADOPTION REQUESTS
    //CREATE
    public AdoptionRequest addAdoptionRequest(AdoptionRequest adoptionRequest) {
        if (adoptionRequest.getRequestID() != null) {
            return addAdoptionRequest(adoptionRequest.getRequestID(), adoptionRequest);
        }
        return addAdoptionRequest(UUID.randomUUID(), adoptionRequest);
    }

    public AdoptionRequest addAdoptionRequest(UUID adoptionRequestID, AdoptionRequest adoptionRequest) {
        adoptionRequests.put(adoptionRequestID, adoptionRequest);
        return adoptionRequest;
    }

    // READ
    public AdoptionRequest getAdoptionRequest(UUID adoptionRequestID) {
        return adoptionRequests.get(adoptionRequestID);
    }

    public Map<UUID, AdoptionRequest> getAllAdoptionRequests() {
        return adoptionRequests;
    }

    //UPDATE
    public boolean setAdoptionRequest(UUID adoptionRequestID, AdoptionRequest adoptionRequest) {
        var theAdoptionRequest = adoptionRequests.get(adoptionRequestID);
        if (theAdoptionRequest == null) {
            return false;
        }
        theAdoptionRequest.replaceWith(adoptionRequest);
        return true;
    }

    //DELETE
    public boolean removeAdoptionRequest(UUID adoptionRequestID) {
        var adoptionRequest = adoptionRequests.get(adoptionRequestID);
        if (adoptionRequest == null) {
            return false;
        }
        adoptionRequests.remove(adoptionRequestID);
        return true;
    }

    // create objects
    private void populateApplicationState() {

        /*
        CREATE PETS
         */

        var pepper = addPet(UUID.fromString("b8d0c81d-e1c6-4708-bd02-d218a23e4805"),
                new Pet("Pepper",
                        "Dog",
                        "Labrador",
                        false,
                        Pet.Gender.FEMALE,
                        "Cute and friendly",
                        "Playful",
                        "black",
                        false,
                        true,
                        true,
                        true,
                        2,
                        200.0,
                        Pet.Status.AVAILABLE,
                        true,
                        true,
                        "None")
        );

        var nala = addPet(UUID.fromString("358e3775-682a-4b85-a2e1-d3bf0632baea"),
                new Pet("Nala",
                        "Dog",
                        "Shih-tzu",
                        true,
                        Pet.Gender.FEMALE,
                        "Cheerful dog",
                        "Independant",
                        "beige",
                        true,
                        true,
                        true,
                        true,
                        11,
                        250.0,
                        Pet.Status.AVAILABLE,
                        true,
                        true,
                        "Cyst on back"));

        var simba = addPet(UUID.fromString("17792447-fd66-464b-b27c-615a7d420d05"),
                new Pet("Simba",
                        "Dog",
                        "Shih-tzu",
                        true,
                        Pet.Gender.MALE,
                        "Cheerful",
                        "Clingy",
                        "beige",
                        true,
                        true,
                        true,
                        true,
                        10,
                        250.0,
                        Pet.Status.AVAILABLE,
                        true,
                        true,
                        "Nearly blind"));

        /*
         CREATE PET OWNERS
         */

        var alice = addPetOwner(UUID.fromString("d79b117e-6cd5-44f0-8ab0-8c87ccda04f0"),
                new PetOwner(
                        "alice@gmail.com",
                        "password123",
                        "Alice",
                        "Gold",
                        new Location(
                                "Paris",
                                "75000",
                                "Champs-elysee"
                        ),
                        User.Role.PET_OWNER
                ));

        var bernard = addPetOwner(
                UUID.fromString("c3498ff2-92af-4bf0-b6a2-6230baba08f6"),
                new PetOwner(
                        "bernard@gmail.com",
                        "password",
                        "Bernard",
                        "Jean",
                        new Location("Chambesy", "1000", "rue la fontaine"),
                        User.Role.PET_OWNER
                ));

        /*
        CREATE ADOPTERS
         */
        var bob = addAdopter(UUID.fromString("312e2c8e-893a-4cbf-b0e3-f1412ad8a9c2"),
                new Adopter(
                "bob@gmail.com",
                "1234",
                "Bob",
                "Sinclar",
                new Location(
                        "Manhattan",
                        "20900",
                        "5th ave"
                ),
                User.Role.ADOPTER
                )
        );

        var jane = addAdopter(UUID.fromString("fb148060-61a6-4ca2-9ba0-ff88317332d0"),
                new Adopter(
                        "jane@gmail.com",
                        "ilovecats",
                        "Jane",
                        "Plane",
                        new Location(
                                "Geneva",
                                "1206",
                                "Rue de la croix d'or"
                        ),
                        User.Role.ADOPTER
                )
        );

        /*
        CREATE ADVERTISEMENTS
         */

        var advertisementForPepper = addAdvertisement(
                UUID.fromString("a30dcf15-6fab-4b55-8ebe-b290fb3509df"),
                new Advertisement(
                        pepper,
                        alice,
                        pepper.getDescription(),
                        alice.getLocation(),
                        Advertisement.Status.AVAILABLE
                )
        );
        var advertisementForSimba = addAdvertisement(
                UUID.fromString("356ba347-299d-48f2-b32e-6bc9144101ec"),
                new Advertisement(
                        simba,
                        bernard,
                        simba.getDescription(),
                        bernard.getLocation(),
                        Advertisement.Status.AVAILABLE
                )
        );

        /*
        CREATE REQUESTS
         */

//        var requestFromBob = addAdoptionRequest(
//                UUID.fromString("58e5f60f-2d88-4c3b-984b-6f50a5f983cd"),
//                new AdoptionRequest(
//                        bob,
//                        advertisementForPepper,
//                        AdoptionRequest.Status.PENDING
//                )
//        );
//
//        var requestFromJane = addAdoptionRequest(
//                UUID.fromString("a63d174b-9954-44f1-b1f2-7c3a6918ec9f"),
//                new AdoptionRequest(
//                        jane,
//                        advertisementForSimba,
//                        AdoptionRequest.Status.PENDING
//                )
//        );

    }
}
