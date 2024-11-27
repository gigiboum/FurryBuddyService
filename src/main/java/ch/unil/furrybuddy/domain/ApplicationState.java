package ch.unil.furrybuddy.domain;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@ApplicationScoped
public class ApplicationState {

    private Map<String, UUID> users;
    private Map<UUID, Adopter> adopters;
    private Map<UUID, PetOwner> petOwners;
    private Map<UUID, Pet> pets;
    private Map<UUID, Advertisement> advertisements;
    private Map<UUID, AdoptionRequest> adoptionRequests;

    @PostConstruct
    public void init() {
        users = new TreeMap<>();
        adopters = new TreeMap<>();
        petOwners = new TreeMap<>();
        pets = new TreeMap<>();
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
        if (!(pets.containsKey(petID))) {
            throw new IllegalArgumentException("Pet with this ID does not exist!");
        }
        return pets.get(petID);
    }

    public Map<UUID, Pet> getAllPets() {
        if (pets.isEmpty()) {
            throw new IllegalArgumentException("There are no pets!");
        }
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

    public PetOwner addPetOwner(UUID petOwnerID, PetOwner petOwner) {
        var email = petOwner.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is null or empty");
        }
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("A user with this email already exists!");
        }
        if (petOwner.getPassword() == null || petOwner.getPassword().isBlank()) {
            throw new IllegalArgumentException("You must enter a password!");
        }

        petOwner.setUserID(petOwnerID);
        petOwners.put(petOwnerID, petOwner);
        users.put(email, petOwnerID);
        return petOwner;
    }

    // READ
    public PetOwner getPetOwner(UUID petOwnerID) {
        if (!(petOwners.containsKey(petOwnerID))) {
            throw new IllegalArgumentException("User with this ID does not exist!");
        }
        return petOwners.get(petOwnerID);
    }

    public Map<UUID, PetOwner> getAllPetOwners() {
        if (petOwners.isEmpty()) {
            throw new IllegalArgumentException("There are no pet owners!");
        }
        return petOwners;
    }

    //UPDATE
    public boolean setPetOwner(UUID petOwnerID, PetOwner petOwner) {
        var thePetOwner = petOwners.get(petOwnerID);
        if (thePetOwner == null) {
            return false;
        }
        thePetOwner.replaceWith(petOwner);
        return true;
    }

    //DELETE
    public boolean removePetOwner(UUID petOwnerID) {
        var petOwner = petOwners.get(petOwnerID);
        if (!(petOwners.containsKey(petOwnerID))) {
            throw new IllegalArgumentException("Pet Owner with this ID does not exist!");
        }
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

    public Adopter addAdopter(UUID adopterID, Adopter adopter) {
        var email = adopter.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is null or empty");
        }
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("A user with this email already exists!");
        }
        if (adopter.getPassword() == null || adopter.getPassword().isBlank()) {
            throw new IllegalArgumentException("You must enter a password!");
        }

        adopter.setUserID(adopterID);
        adopters.put(adopterID, adopter);
        users.put(email, adopterID);
        return adopter;
    }

    // READ
    public Adopter getAdopter(UUID adopterID) {
        if (!(adopters.containsKey(adopterID))) {
            throw new IllegalArgumentException("User with this ID does not exist!");
        }
        return adopters.get(adopterID);
    }

    public Map<UUID, Adopter> getAllAdopters() {
        if (adopters.isEmpty()) {
            throw new IllegalArgumentException("There are no adopters!");
        }
        return adopters;
    }

    //UPDATE
    public boolean setAdopter(UUID adopterID, Adopter adopter) {
        var theAdopter = adopters.get(adopterID);
        if (theAdopter == null) {
            return false;
        }
        theAdopter.replaceWith(adopter);
        return true;
    }

    //DELETE
    public boolean removeAdopter(UUID adopterID) {
        var adopter = adopters.get(adopterID);

        if (!(adopters.containsKey(adopterID))) {
            throw new IllegalArgumentException("Adopter with this ID does not exist!");
        }
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
//        var pet = advertisement.getPet();
//        if(pets.containsKey(pet.getPetID())) {
//            throw new IllegalArgumentException("Pet with this ID already exists!");
//        }
        advertisement.setAdvertisementID(advertisementID);
        advertisements.put(advertisementID, advertisement);
        return advertisement;
    }

    // READ
    public Advertisement getAdvertisement(UUID advertisementID) {
        if (!(advertisements.containsKey(advertisementID))) {
            throw new IllegalArgumentException("Advertisement with this id does not exist!");
        }
        return advertisements.get(advertisementID);
    }

    public Map<UUID, Advertisement> getAllAds() {
        if (advertisements.isEmpty()) {
            throw new IllegalArgumentException("No advertisements found!");
        }
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
        adoptionRequest.setRequestID(adoptionRequestID);
        adoptionRequests.put(adoptionRequestID, adoptionRequest);
        return adoptionRequest;
    }

    // READ
    public AdoptionRequest getAdoptionRequest(UUID adoptionRequestID) {
        if (!(adoptionRequests.containsKey(adoptionRequestID))) {
            throw new IllegalArgumentException("No advertisement with this ID found!");
        }
        return adoptionRequests.get(adoptionRequestID);
    }

    public Map<UUID, AdoptionRequest> getAllAdoptionRequests() {
        if (adoptionRequests.isEmpty()) {
            throw new IllegalArgumentException("No adoption requests found!");
        }
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
        if (!(adoptionRequests.containsKey(adoptionRequestID))) {
            throw new IllegalArgumentException("Adoption request with this ID does not exist!");
        }
        if (adoptionRequest == null) {
            return false;
        }
        adoptionRequests.remove(adoptionRequestID);
        return true;
    }

    //authentication
    public UUID authenticate(String email, String password, boolean isPetOwner) {
        var uuid = users.get(email);
        if (uuid == null) {
            return null;
        }
        User user;
        if (isPetOwner) {
            user = petOwners.get(uuid);
        } else {
            user = adopters.get(uuid);
        }
        if (user == null) {
            return null;
        }
        // Check if the provided password matches
        if (user.getPassword().equals(password)) {
            return uuid; // Authentication successful
        }
        throw new IllegalArgumentException("Incorrect password or email");
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
        CREATE ADVERTISEMENT
         */

        var advertisementForSimba = bernard.createAdvertisement(simba);
        addAdvertisement(UUID.fromString("356ba347-299d-48f2-b32e-6bc9144101ec"), advertisementForSimba);


        /*
        CREATE REQUESTS
         */
        var requestFromBob = addAdoptionRequest(
                UUID.fromString("58e5f60f-2d88-4c3b-984b-6f50a5f983cd"),
                new AdoptionRequest(
                        bob.getUserID(),
                        advertisementForSimba,
                        AdoptionRequest.Status.PENDING
                )
        );

        var requestFromJane = addAdoptionRequest(
                UUID.fromString("a63d174b-9954-44f1-b1f2-7c3a6918ec9f"),
                new AdoptionRequest(
                        jane.getUserID(),
                        advertisementForSimba,
                        AdoptionRequest.Status.PENDING
                )
        );

    }
}
