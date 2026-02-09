import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:campus_events_app/pages/detail_page.dart';
import 'package:campus_events_app/pages/favories.dart';
import 'package:campus_events_app/utils.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFE8ECF4),
      body: SafeArea(
        child: StreamBuilder<QuerySnapshot>(
          stream: FirebaseFirestore.instance.collection("events").snapshots(),
          builder: (context, snapshot) {
            if (snapshot.hasError) {
              return const Center(child: Text("Une erreur est survenue"));
            }
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            }

            final data = snapshot.requireData;
            final featuredEvents = data.docs
                .where((doc) => doc['is_featured'] == true)
                .toList();
            final allEvents = data.docs;

            return SingleChildScrollView(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildHeader(),
                  const SizedBox(height: 20),
                  if (featuredEvents.isNotEmpty)
                    _buildFeaturedSection(featuredEvents),
                  _buildAllEventsSection(allEvents),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  // --- WIDGETS UTILITAIRES ---

  Widget _buildHeader() {
    return Center(
      child: Column(
        children: [
          Image.asset("assets/images/logorond.jpg", height: 40),
          const SizedBox(height: 5),
          const Text(
            "Campus Events",
            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
          ),
          const SizedBox(height: 20),
          const Text(
            "Que voulez vous faire aujourd'hui ?",
            style: TextStyle(
              fontSize: 22,
              fontWeight: FontWeight.bold,
              color: Colors.black87,
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }

  Widget _buildFeaturedSection(List<DocumentSnapshot> events) {
    double screenWidth = MediaQuery.of(context).size.width;
    double screenHeight = MediaQuery.of(context).size.height;

    return Column(
      children: [
        _buildSectionHeader("À la une"),
        const SizedBox(height: 15),
        SizedBox(
          height: screenHeight * 0.35,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            itemCount: events.length,
            itemBuilder: (context, index) {
              return _buildFeaturedCard(
                events[index],
                screenWidth,
                screenHeight,
              );
            },
          ),
        ),
        const SizedBox(height: 25),
      ],
    );
  }

  Widget _buildAllEventsSection(List<DocumentSnapshot> events) {
    return Column(
      children: [
        _buildSectionHeader("Tous les événements"),
        const SizedBox(height: 15),
        ListView.builder(
          physics: const NeverScrollableScrollPhysics(),
          shrinkWrap: true,
          itemCount: events.length,
          itemBuilder: (context, index) => _buildWideCard(events[index]),
        ),
      ],
    );
  }

  Widget _buildSectionHeader(String title) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          title,
          style: const TextStyle(
            fontSize: 20,
            fontWeight: FontWeight.bold,
            color: Colors.black87,
          ),
        ),
      ],
    );
  }

  // Carte "A la une" (Grande carte verticale avec image en haut)
  Widget _buildFeaturedCard(
    DocumentSnapshot doc,
    double screenWidth,
    double screenHeight,
  ) {
    Map<String, dynamic> event = doc.data() as Map<String, dynamic>;
    return GestureDetector(
      onTap: () => _navigateToDetail(event),
      child: Container(
        width: screenWidth * 0.6, // Largeur dynamique (~60% de l'écran)
        margin: const EdgeInsets.only(right: 15, bottom: 10),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
              offset: const Offset(0, 5),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Stack(
              clipBehavior: Clip.none,
              children: [
                // Image
                ClipRRect(
                  borderRadius: const BorderRadius.vertical(
                    top: Radius.circular(20),
                  ),
                  child: displayImage(
                    event["image_url"],
                    height: screenHeight * 0.2,
                    width: double.infinity,
                  ),
                ),
                // Bouton Favoris (Flottant)
                Positioned(
                  bottom: -15,
                  right: 15,
                  child: GestureDetector(
                    onTap: () => _addToFavorites(event),
                    child: Container(
                      height: 40,
                      width: 40,
                      decoration: BoxDecoration(
                        color: Colors.orange,
                        borderRadius: BorderRadius.circular(10),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.orange.withOpacity(0.4),
                            blurRadius: 5,
                            offset: const Offset(0, 3),
                          ),
                        ],
                      ),
                      child: const Icon(
                        Icons.bookmark,
                        color: Colors.white,
                        size: 20,
                      ),
                    ),
                  ),
                ),
              ],
            ),

            Padding(
              padding: const EdgeInsets.fromLTRB(12, 25, 12, 12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    event["title"] ?? "Sans titre",
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 18,
                    ),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Carte "Wide" (Image à gauche, texte à droite)
  Widget _buildWideCard(DocumentSnapshot doc) {
    Map<String, dynamic> event = doc.data() as Map<String, dynamic>;
    return GestureDetector(
      onTap: () => _navigateToDetail(event),
      child: Container(
        margin: const EdgeInsets.only(bottom: 15),
        padding: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
              offset: const Offset(0, 5),
            ),
          ],
        ),
        child: Row(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(15),
              child: displayImage(event["image_url"], width: 80, height: 80),
            ),
            const SizedBox(width: 15),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    event["title"] ?? "Sans titre",
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                  const SizedBox(height: 5),
                  if (event.containsKey("date")) ...[
                    Text(
                      event["date"] is Timestamp
                          ? "${(event["date"] as Timestamp).toDate().day}/${(event["date"] as Timestamp).toDate().month}"
                          : "Date inconnue",
                      style: const TextStyle(
                        color: Colors.blueAccent,
                        fontSize: 12,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 5),
                  ],
                  Text(
                    event["description"] ?? "",
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(color: Colors.grey[600], fontSize: 12),
                  ),
                ],
              ),
            ),
            IconButton(
              icon: const Icon(Icons.favorite_border, color: Colors.grey),
              onPressed: () => _addToFavorites(event),
            ),
          ],
        ),
      ),
    );
  }

  void _navigateToDetail(Map<String, dynamic> event) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => DetailPage(
          imagePath: event["image_url"] ?? "assets/images/image5.jpg",
          title: event["title"] ?? "Sans titre",
          description: event["description"] ?? "",
        ),
      ),
    );
  }

  void _addToFavorites(Map<String, dynamic> event) {
    // Conversion en Map<String, String> pour compatibilité avec favories.dart
    Map<String, String> favEvent = {
      "image": event["image_url"]?.toString() ?? "assets/images/image5.jpg",
      "title": event["title"]?.toString() ?? "Sans titre",
      "description": event["description"]?.toString() ?? "",
    };
    setState(() {
      Favories.favoriteItems.add(favEvent);
    });
    showMessage(context, "${event["title"]} ajouté aux favoris");
  }
}
