package com.tripmate.service;

import com.tripmate.model.Destination;
import com.tripmate.model.GroupTrip;
import com.tripmate.model.SoloTrip;
import com.tripmate.model.User;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service managing destination catalog and core budget-based recommendation logic.
 * Demonstrates business logic implemented strictly in Java.
 */
@Service
public class DestinationService {

    // In-memory data store using HashMap and ArrayList as per technology requirements
    private final Map<String, Destination> destinationMap = new LinkedHashMap<>();

    @PostConstruct
    public void initDestinations() {
        addDestination(new Destination(
                "nandi-hills",
                "Nandi Hills",
                "Karnataka",
                "Ancient hill fortress offering breathtaking sunrise views, cool misty climate, Tipu's Drop, and historic temples located just outside Bengaluru.",
                500, 1000, 400, 300, 1,
                Arrays.asList("Tipu's Drop", "Nandi Temple", "Sunrise Viewpoint", "Amrita Sarovar"),
                "https://images.unsplash.com/photo-1596176530529-78163a4f7af2?auto=format&fit=crop&w=800&q=80",
                "Weekend Getaway"
        ));

        addDestination(new Destination(
                "mysore",
                "Mysore",
                "Karnataka",
                "The City of Palaces renowned for royal heritage, illuminated Mysore Palace, Chamundi Hills, silk sarees, and aromatic sandalwood.",
                900, 1200, 500, 500, 3,
                Arrays.asList("Mysore Palace", "Chamundi Hills", "Brindavan Gardens", "Mysore Zoo", "St. Philomena's Church"),
                "https://images.unsplash.com/photo-1600100397608-f010f443834a?auto=format&fit=crop&w=800&q=80",
                "Heritage & Culture"
        ));

        addDestination(new Destination(
                "hampi",
                "Hampi",
                "Karnataka",
                "UNESCO World Heritage Site with dramatic boulder-strewn landscapes and ancient ruins of the magnificent Vijayanagara Empire.",
                1200, 1100, 450, 450, 2,
                Arrays.asList("Virupaksha Temple", "Vijaya Vittala Temple", "Stone Chariot", "Matanga Hill Sunrise", "Lotus Mahal"),
                "https://images.unsplash.com/photo-1609137144822-498c471017cb?auto=format&fit=crop&w=800&q=80",
                "UNESCO Heritage"
        ));

        addDestination(new Destination(
                "chikmagalur",
                "Chikmagalur",
                "Karnataka",
                "The Coffee Capital of Karnataka nestled in the Western Ghats, featuring Mullayanagiri, Karnataka's highest peak, and lush green valleys.",
                1200, 1600, 550, 600, 2,
                Arrays.asList("Mullayanagiri Peak", "Baba Budangiri", "Hebbe Falls", "Coffee Plantations", "Z Point"),
                "https://images.unsplash.com/photo-1544735716-392fe2489ffa?auto=format&fit=crop&w=800&q=80",
                "Hill Station"
        ));

        addDestination(new Destination(
                "coorg",
                "Coorg (Kodagu)",
                "Karnataka",
                "Scotland of India, famed for aromatic coffee estates, spice plantations, misty mountain passes, Abbey Falls, and rich Kodava culture.",
                1400, 1800, 600, 700, 3,
                Arrays.asList("Abbey Falls", "Raja's Seat", "Dubare Elephant Camp", "Talakaveri", "Namdroling Golden Temple"),
                "https://images.unsplash.com/photo-1590766940554-634a7ed41450?auto=format&fit=crop&w=800&q=80",
                "Nature & Coffee"
        ));

        addDestination(new Destination(
                "gokarna",
                "Gokarna",
                "Karnataka",
                "Serene beach town with untouched coastline, famous beach treks connecting Om Beach, Kudle Beach, and spiritual Mahabaleshwar temple.",
                1300, 1300, 500, 450, 2,
                Arrays.asList("Om Beach", "Kudle Beach", "Half Moon Beach", "Mahabaleshwar Temple", "Mirjan Fort"),
                "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=800&q=80",
                "Beach & Trek"
        ));

        addDestination(new Destination(
                "hyderabad",
                "Hyderabad",
                "Telangana",
                "City of Pearls combining 400-year-old Nizami history, magnificent Golconda Fort, Charminar, and world-renowned Hyderabadi Biryani.",
                1100, 1500, 650, 600, 2,
                Arrays.asList("Charminar", "Golconda Fort", "Ramoji Film City", "Hussain Sagar Lake", "Salar Jung Museum"),
                "https://images.unsplash.com/photo-1605007493699-ce65834f8a00?auto=format&fit=crop&w=800&q=80",
                "City & Cuisine"
        ));

        addDestination(new Destination(
                "pondicherry",
                "Pondicherry",
                "Puducherry",
                "Charming coastal haven with French colonial quarters, vibrant bougainvillea, Promenade Beach, Auroville, and tranquil cafes.",
                1200, 1700, 650, 550, 2,
                Arrays.asList("Promenade Beach", "French Quarter (White Town)", "Auroville", "Paradise Beach", "Sri Aurobindo Ashram"),
                "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=800&q=80",
                "Coastal Heritage"
        ));

        addDestination(new Destination(
                "wayanad",
                "Wayanad",
                "Kerala",
                "Lush green spice district in God's Own Country with mist-capped mountains, Edakkal Caves with petroglyphs, and Banasura Sagar dam.",
                1400, 1700, 600, 650, 3,
                Arrays.asList("Banasura Sagar Dam", "Edakkal Caves", "Chembra Peak & Heart Lake", "Soochipara Falls", "Pookode Lake"),
                "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=800&q=80",
                "Nature & Wildlife"
        ));

        addDestination(new Destination(
                "ooty",
                "Ooty",
                "Tamil Nadu",
                "Queen of Nilgiri Hill Stations, celebrated for its UNESCO Nilgiri Mountain Railway, Botanical Gardens, tea estates, and Dodabetta peak.",
                1500, 1900, 650, 700, 3,
                Arrays.asList("Nilgiri Toy Train", "Ooty Lake & Boating", "Botanical Gardens", "Doddabetta Peak", "Tea Factory Museum"),
                "https://images.unsplash.com/photo-1589182373726-e4f658ab50f0?auto=format&fit=crop&w=800&q=80",
                "Hill Station"
        ));

        addDestination(new Destination(
                "kodaikanal",
                "Kodaikanal",
                "Tamil Nadu",
                "The Princess of Hill Stations with rolling wooded slopes, star-shaped Kodai Lake, Coaker's Walk, and enchanting Pine Forests.",
                1600, 1900, 600, 600, 3,
                Arrays.asList("Kodai Lake", "Coaker's Walk", "Pillar Rocks", "Silver Cascade Falls", "Pine Forest"),
                "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80",
                "Romantic Hills"
        ));

        addDestination(new Destination(
                "munnar",
                "Munnar",
                "Kerala",
                "Sprawling emerald tea estates, mist-blanketed valleys, Anamudi peak, and serene waterfalls in the high ranges of Kerala.",
                1800, 2000, 700, 750, 3,
                Arrays.asList("Tea Gardens", "Mattupetty Dam", "Eravikulam National Park", "Top Station", "Attukad Waterfalls"),
                "https://images.unsplash.com/photo-1516483638261-f4dbaf036963?auto=format&fit=crop&w=800&q=80",
                "Tea & Hills"
        ));

        addDestination(new Destination(
                "goa",
                "Goa",
                "Goa",
                "India's party and beach paradise offering sun-kissed sands, Portuguese baroque churches, water sports, vibrant shacks, and river cruises.",
                2200, 2400, 850, 800, 3,
                Arrays.asList("Baga Beach", "Basilica of Bom Jesus", "Aguada Fort", "Dudhsagar Waterfalls", "Mandovi River Cruise"),
                "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=800&q=80",
                "Beach & Nightlife"
        ));

        addDestination(new Destination(
                "rishikesh",
                "Rishikesh",
                "Uttarakhand",
                "Yoga Capital of the World along the holy Ganges, famous for white-water rafting, Lakshman Jhula, bungee jumping, and sacred evening Ganga Aarti.",
                2800, 2100, 750, 850, 3,
                Arrays.asList("Ganga River Rafting", "Triveni Ghat Evening Aarti", "Beatles Ashram", "Lakshman Jhula", "Shivpuri Camping"),
                "https://images.unsplash.com/photo-1545648877-3807663e264d?auto=format&fit=crop&w=800&q=80",
                "Spiritual & Adventure"
        ));

        addDestination(new Destination(
                "manali",
                "Manali",
                "Himachal Pradesh",
                "Himalayan resort town flanked by snow-draped peaks, Solang Valley adventure sports, Rohtang Pass, apple orchards, and pine forests.",
                3500, 2500, 800, 950, 4,
                Arrays.asList("Solang Valley", "Rohtang Pass Snow Point", "Hadimba Temple", "Old Manali Cafes", "Jogini Waterfall"),
                "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=800&q=80",
                "Snow & Himalayas"
        ));
    }

    private void addDestination(Destination destination) {
        destinationMap.put(destination.getId(), destination);
    }

    public List<Destination> getAllDestinations() {
        return new ArrayList<>(destinationMap.values());
    }

    public Destination getDestinationById(String id) {
        return destinationMap.get(id);
    }

    /**
     * Core Java recommendation engine:
     * Calculates trip cost dynamically using polymorphism (SoloTrip vs GroupTrip)
     * and filters destinations that strictly fit within the user's budget.
     */
    public List<Destination> getRecommendedDestinations(double totalBudget, User user) {
        if (user == null || totalBudget <= 0) {
            return Collections.emptyList();
        }

        List<Destination> recommended = new ArrayList<>();
        boolean isGroup = user.isGroup();

        for (Destination dest : destinationMap.values()) {
            double estimatedCost;
            if (isGroup) {
                GroupTrip testGroupTrip = new GroupTrip("TEMP", user, dest, totalBudget);
                estimatedCost = testGroupTrip.calculateTotalCost();
            } else {
                SoloTrip testSoloTrip = new SoloTrip("TEMP", user, dest, totalBudget);
                estimatedCost = testSoloTrip.calculateTotalCost();
            }

            // Destination qualifies if estimated cost <= available budget
            if (estimatedCost <= totalBudget) {
                recommended.add(dest);
            }
        }

        // Sort by how closely it fits the budget (best value utilization first)
        recommended.sort(Comparator.comparingDouble(Destination::getEstimatedCostPerPerson));
        return recommended;
    }

    /**
     * Finds nearby / lowest cost alternatives when no destination fits within the entered budget.
     */
    public List<Destination> getAffordableAlternatives(double totalBudget, User user, int limit) {
        List<Destination> all = new ArrayList<>(destinationMap.values());
        all.sort(Comparator.comparingDouble(Destination::getEstimatedCostPerPerson));
        return all.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * Searches destinations by name, state, or tag.
     */
    public List<Destination> searchDestinations(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDestinations();
        }
        String q = query.trim().toLowerCase();
        return destinationMap.values().stream()
                .filter(d -> d.getName().toLowerCase().contains(q)
                        || d.getState().toLowerCase().contains(q)
                        || d.getTag().toLowerCase().contains(q)
                        || d.getDescription().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}

