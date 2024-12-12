package ch.unil.furrybuddy.domain;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApplicationState {

    private Map<String, UUID> users;
    private Map<UUID, Adopter> adopters;
    private Map<UUID, PetOwner> petOwners;
    private Map<UUID, Pet> pets;
    private Map<UUID, Advertisement> advertisements;
    private Map<UUID, AdoptionRequest> adoptionRequests;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        users = new TreeMap<>();
        adopters = new TreeMap<>();
        petOwners = new TreeMap<>();
        pets = new TreeMap<>();
        advertisements = new TreeMap<>();
        adoptionRequests = new TreeMap<>();

        var allAdopters = findAllAdopters();
        for (var adopter : allAdopters) {
            adopters.put(adopter.getUserID(), adopter);
            users.put(adopter.getEmail(), adopter.getUserID());
        }

        var allPetOwners = findAllPetOwners();
        for (var petowner : allPetOwners) {
            petOwners.put(petowner.getUserID(), petowner);
            users.put(petowner.getEmail(), petowner.getUserID());
        }

        var allAdvertisements = findAllAdvertisements();
        for(var advertisement : allAdvertisements){
            advertisements.put(advertisement.getAdvertisementID(), advertisement);
        }

        var allAdoptionRequests = findAllAdoptionRequests();
        for(var adoptionRequest : allAdoptionRequests){
            adoptionRequests.put(adoptionRequest.getRequestID(), adoptionRequest);
        }

        var allPets = findAllPets();
        for(var pet : allPets){
            pets.put(pet.getPetID(), pet);
        }
    }

    // DB
    private void clearObjects() {
        adopters.clear();
        petOwners.clear();
        users.clear();
        pets.clear();
        advertisements.clear();
        adoptionRequests.clear();
    }

    private void clearTables() {
        // Disable foreign key checks
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        // Get all table names in the studybuddy schema
        List<String> tables = (List<String>) em
                .createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'furrybuddy'")
                .getResultList();

        // Avoid removing the sequence table
        tables.remove("SEQUENCE");

        // Truncate each table
        for (String table : tables) {
            em.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
        }

        // Re-enable foreign key checks
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    public List<Adopter> findAllAdopters() {
        return em.createQuery("SELECT c FROM Adopter c", Adopter.class).getResultList();
    }

    public List<PetOwner> findAllPetOwners() {
        return em.createQuery("SELECT c FROM PetOwner c", PetOwner.class).getResultList();
    }

    public List<Advertisement> findAllAdvertisements() {
        return em.createQuery("SELECT c FROM Advertisement c", Advertisement.class).getResultList();
    }

    public List<AdoptionRequest> findAllAdoptionRequests() {
        return em.createQuery("SELECT c FROM AdoptionRequest c", AdoptionRequest.class).getResultList();
    }

    public List<Pet> findAllPets() {
        return em.createQuery("SELECT c FROM Pet c", Pet.class).getResultList();
    }

    @Transactional
    public void clearDB() {
        clearObjects();
        clearTables();
    }

    @Transactional
    public void populateDB() {
        clearObjects();
        populateApplicationState();
        for (var adopter : adopters.values()) {
            em.persist(adopter);
        }
        for (var petowner : petOwners.values()) {
            em.persist(petowner);
        }
        for (var adoptionRequest: adoptionRequests.values()){
            em.persist(adoptionRequest);
        }
        for (var advertisement : advertisements.values()){
            em.persist(advertisement);
        }
    }

    @Transactional
    public void resetDB() {
        clearDB();
        populateDB();
    }


    // PET
    // CREATE
    @Transactional
    public Pet addPet(Pet pet) {
        if (pet.getPetID() != null) {
            return addPet(pet.getPetID(), pet);
        }
        return addPet(UUID.randomUUID(), pet);
    }

    @Transactional
    public Pet addPet(UUID petID, Pet pet) {
        pet.setPetID(petID);
        pets.put(petID, pet);
        em.persist(pet);
        return pet;
    }

    // READ
    public Pet getPet(UUID petID) {
        if (!(pets.containsKey(petID))) {
            throw new IllegalArgumentException("Pet with this ID does not exist!");
        }
        return pets.get(petID);
    }
    public boolean hasPet(UUID petID) {
        return pets.containsKey(petID);
    }

    public Map<UUID, Pet> getAllPets() {
        if (pets.isEmpty()) {
            throw new IllegalArgumentException("There are no pets!");
        }
        return pets;
    }

    // UPDATE
    @Transactional
    public boolean setPet(UUID petID, Pet pet) {
        var thePet = pets.get(petID);
        if (thePet == null) {
            return false;
        }
        thePet.replaceWith(pet);
        em.merge(pet);
        return true;
    }

    // DELETE
    @Transactional
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
    @Transactional
    public PetOwner addPetOwner(PetOwner petOwner) {
        if (petOwner.getUserID() != null) {
            return addPetOwner(petOwner.getUserID(), petOwner);
        }
        return addPetOwner(UUID.randomUUID(), petOwner);
    }

    @Transactional
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
        em.persist(petOwner);
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
    @Transactional
    public boolean setPetOwner(UUID petOwnerID, PetOwner petOwner) {
        var thePetOwner = petOwners.get(petOwnerID);
        if (thePetOwner == null) {
            return false;
        }
        thePetOwner.replaceWith(petOwner);
        em.merge(petOwner);
        return true;
    }

    //DELETE
    @Transactional
    public boolean removePetOwner(UUID petOwnerID) {
        var petOwner = petOwners.get(petOwnerID);
        if (!(petOwners.containsKey(petOwnerID))) {
            throw new IllegalArgumentException("Pet Owner with this ID does not exist!");
        }
        if (petOwner == null) {
            return false;
        }
        petOwner = em.merge(petOwner);

        List<Advertisement> advertisements = em.createQuery(
                        "SELECT ad FROM Advertisement ad WHERE ad.petOwnerID = :petOwner",
                        Advertisement.class)
                .setParameter("petOwner", petOwner.getUserID())
                .getResultList();

        for (Advertisement advertisement : advertisements) {
            removeAdvertisement(advertisement.getAdvertisementID());
            em.remove(advertisement); // Delete each AdoptionRequest
        }

        em.remove(petOwner);
        petOwners.remove(petOwnerID);
        return true;
    }

    // ADOPTER
    // CREATE
    @Transactional
    public Adopter addAdopter(Adopter adopter) {
        if (adopter.getUserID() != null) {
            return addAdopter(adopter.getUserID(), adopter);
        }
        return addAdopter(UUID.randomUUID(), adopter);
    }

    @Transactional
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
        em.persist(adopter);
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
    @Transactional
    public boolean setAdopter(UUID adopterID, Adopter adopter) {
        var theAdopter = adopters.get(adopterID);
        if (theAdopter == null) {
            return false;
        }
        theAdopter.replaceWith(adopter);
        em.merge(adopter);
        return true;
    }

    //DELETE
    @Transactional
    public boolean removeAdopter(UUID adopterID) {
        var adopter = adopters.get(adopterID);

        if (!(adopters.containsKey(adopterID))) {
            throw new IllegalArgumentException("Adopter with this ID does not exist!");
        }
        if (adopter == null) {
            return false;
        }
        adopter = em.merge(adopter);

        List<AdoptionRequest> adoptionRequests = em.createQuery(
                        "SELECT ar FROM AdoptionRequest ar WHERE ar.adopterID = :adopter",
                        AdoptionRequest.class)
                .setParameter("adopter", adopter.getUserID())
                .getResultList();

        for (AdoptionRequest adoptionRequest : adoptionRequests) {
            removeAdoptionRequest(adoptionRequest.getRequestID());
            em.remove(adoptionRequest); // Delete each AdoptionRequest
        }

        em.remove(adopter);
        adopters.remove(adopterID);
        return true;
    }

    //ADVERTISEMENTS
    //CREATE
    @Transactional
    public Advertisement addAdvertisement(Advertisement advertisement) {
//        PetOwner petOwner = em.find(PetOwner.class, advertisement.getPetOwnerID());
//        if (petOwner == null) {
//            throw new EntityNotFoundException("PetOwner not found with ID: " + advertisement.getPetOwnerID());
//        }
        if (advertisements.containsValue(advertisement)) {
            throw new IllegalArgumentException("Advertisement already exists!");
        }
        if (advertisement.getAdvertisementID() != null) {
            return addAdvertisement(advertisement.getAdvertisementID(), advertisement);
        }
        return addAdvertisement(UUID.randomUUID(), advertisement);
    }

    @Transactional
    public Advertisement addAdvertisement(UUID advertisementID, Advertisement advertisement) {
        advertisement.setAdvertisementID(advertisementID);

        PetOwner petOwner = getPetOwner(advertisement.getPetOwnerID());
        advertisements.put(advertisementID, advertisement);
        em.merge(petOwner);
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
    @Transactional
    public boolean setAdvertisement(UUID advertisementID, Advertisement advertisement) {
//        var theAdvertisement = advertisements.get(advertisementID);
        var theAdvertisement = em.find(Advertisement.class, advertisementID);
        if (theAdvertisement == null) {
            return false;
        }
        theAdvertisement.replaceWith(advertisement);
        em.merge(advertisement);
        return true;
    }

    //DELETE
    @Transactional
    public boolean removeAdvertisement(UUID advertisementID) {
        var advertisement = advertisements.get(advertisementID);
        if (advertisement == null) {
            return false;
        }

        // Remove associated AdoptionRequests
        List<AdoptionRequest> adoptionRequests = em.createQuery(
                        "SELECT ar FROM AdoptionRequest ar WHERE ar.advertisement = :advertisement",
                        AdoptionRequest.class)
                .setParameter("advertisement", advertisement)
                .getResultList();

        for (AdoptionRequest request : adoptionRequests) {
            em.remove(request); // Delete each AdoptionRequest
        }

        PetOwner petOwner = getPetOwner(advertisement.getPetOwnerID());
        petOwner.deleteAdvertisement(advertisement);
        advertisements.remove(advertisementID);
        em.merge(petOwner);
        return true;
    }

    //FILTER
    public List<Advertisement> filterAdvertisements(String species, String breed, String gender, List<String> compatibility) {
        return advertisements.values().stream()
                .filter(ad -> species == null || species.isEmpty() || species.equals(ad.getPet().getSpecies()))
                .filter(ad -> breed == null || breed.isEmpty() || breed.equals(ad.getPet().getBreed()))
                .filter(ad -> gender == null || gender.isEmpty() || matchesGender(ad.getPet().getGender(), gender))
                .filter(ad -> matchesCompatibility(ad.getPet(), compatibility))
                .collect(Collectors.toList());
    }

    private boolean matchesGender(Pet.Gender petGender, String selectedGender) {
        return selectedGender == null || selectedGender.isEmpty() ||
                (petGender != null && petGender.name().equalsIgnoreCase(selectedGender));
    }

    private boolean matchesCompatibility(Pet pet, List<String> compatibility) {
        if (compatibility == null || compatibility.isEmpty()) {
            return true; // No compatibility filter applied
        }

        boolean match = true;
        if (compatibility.contains("Good with Kids")) {
            match &= pet.isCompatibleWithKids();
        }
        if (compatibility.contains("Good with Other Animals")) {
            match &= pet.isCompatibleWithOtherAnimals();
        }
        if (compatibility.contains("Suitable for Inexperienced Owners")) {
            match &= pet.isCompatibleWithInexperiencedOwners();
        }
        if (compatibility.contains("Suitable for Families")) {
            match &= pet.isCompatibleWithFamilies();
        }
        return match;
    }

    //ADOPTION REQUESTS
    //CREATE
    @Transactional
    public AdoptionRequest addAdoptionRequest(AdoptionRequest adoptionRequest) {
        if (adoptionRequest.getRequestID() != null) {
            return addAdoptionRequest(adoptionRequest.getRequestID(), adoptionRequest);
        }
        return addAdoptionRequest(UUID.randomUUID(), adoptionRequest);
    }

    @Transactional
    public AdoptionRequest addAdoptionRequest(UUID adoptionRequestID, AdoptionRequest adoptionRequest) {
        adoptionRequest.setRequestID(adoptionRequestID);
        adoptionRequest.setMessage(adoptionRequest.getMessage());
        adoptionRequests.put(adoptionRequestID, adoptionRequest);

        Adopter adopter = getAdopter(adoptionRequest.getAdopterID());
        em.merge(adopter);
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
    @Transactional
    public boolean setAdoptionRequest(UUID adoptionRequestID, AdoptionRequest adoptionRequest) {
        var theAdoptionRequest = adoptionRequests.get(adoptionRequestID);
        if (theAdoptionRequest == null) {
            return false;
        }
        theAdoptionRequest.replaceWith(adoptionRequest);
        em.merge(adoptionRequest);
        return true;
    }

    @Transactional
    public AdoptionRequest cancelAdoptionRequest(AdoptionRequest adoptionRequest) {
        // Fetch the AdoptionRequest
         AdoptionRequest managedRequest = em.find(AdoptionRequest.class, adoptionRequest.getRequestID());
            if (managedRequest == null) {
                throw new EntityNotFoundException("AdoptionRequest not found");
            }
        // Fetch the Adopter
        var adopter = getAdopter(adoptionRequest.getAdopterID());
        var managedAdopter = em.find(Adopter.class, managedRequest.getAdopterID());
        if (managedAdopter == null) {
            throw new EntityNotFoundException("Adopter not found");
        }

        // Cancel the request
        adopter.cancelAdoptionRequest(managedRequest);
        return managedRequest;
    }

    @Transactional
    public AdoptionRequest acceptAdoptionRequest(AdoptionRequest adoptionRequest) {
        // Fetch the AdoptionRequest
        AdoptionRequest managedRequest = em.find(AdoptionRequest.class, adoptionRequest.getRequestID());
        if (managedRequest == null) {
            throw new EntityNotFoundException("AdoptionRequest not found");
        }
        // Fetch the Adopter
        var petowner = getPetOwner(adoptionRequest.getAdvertisement().getPetOwnerID());
        var managedPetOwner = em.find(PetOwner.class, petowner.getUserID());
        if (managedPetOwner == null) {
            throw new EntityNotFoundException("PetOwner not found");
        }

        // Cancel the request
        petowner.acceptRequest(managedRequest);
        return managedRequest;
    }

    @Transactional
    public AdoptionRequest rejecttAdoptionRequest(AdoptionRequest adoptionRequest) {
        // Fetch the AdoptionRequest
        AdoptionRequest managedRequest = em.find(AdoptionRequest.class, adoptionRequest.getRequestID());
        if (managedRequest == null) {
            throw new EntityNotFoundException("AdoptionRequest not found");
        }
        // Fetch the Adopter
        var petowner = getPetOwner(adoptionRequest.getAdvertisement().getPetOwnerID());
        var managedPetOwner = em.find(PetOwner.class, petowner.getUserID());
        if (managedPetOwner == null) {
            throw new EntityNotFoundException("PetOwner not found");
        }

        // Cancel the request
        petowner.rejectRequest(managedRequest);
        return managedRequest;
    }

    //DELETE
    @Transactional
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

        var advertisementForNala = bernard.createAdvertisement(nala);
        addAdvertisement(UUID.fromString("f2a84043-a4bc-42d8-a6c0-21d5719ed0b1"), advertisementForNala);

        var advertisementForPepper = alice.createAdvertisement(pepper);
        addAdvertisement(UUID.fromString("7e6d6935-c2e3-4d2b-b649-72e0a16a1eb1"), advertisementForPepper);

        /*
        CREATE REQUESTS
         */
        var requestFromJane = jane.createAdoptionRequest(advertisementForSimba, "hello, i am interested in adopting this dog. Please contact me at +41 77 888 43 43");
        addAdoptionRequest(UUID.fromString("a63d174b-9954-44f1-b1f2-7c3a6918ec9f"), requestFromJane);

        var requestFromBob = bob.createAdoptionRequest(advertisementForSimba, "Hi there! Very much interested in Simba!");
        addAdoptionRequest(UUID.fromString("58e5f60f-2d88-4c3b-984b-6f50a5f983cd"), requestFromBob);

    }
}
