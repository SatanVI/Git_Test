import 'package:flutter/material.dart';
import 'package:campus_events_app/pages/detail_page.dart';
import 'package:campus_events_app/utils.dart';

class Favories extends StatefulWidget {
  const Favories({super.key});

  @override
  State<Favories> createState() => _FavoritesState();

  // Liste statique pour stocker les favoris
  static List<Map<String, String>> favoriteItems = [];
}

class _FavoritesState extends State<Favories> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F6F8), // Gris clair du thème
      body: SafeArea(
        child: Column(
          children: [
            _buildHeader(),
            Expanded(
              child: Favories.favoriteItems.isEmpty
                  ? _buildEmptyState()
                  : _buildFavoritesList(),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildHeader() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
      child: Row(
        children: [
          GestureDetector(
            onTap: () => Navigator.pop(context),
            child: Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                border: Border.all(color: Colors.grey.shade300),
                borderRadius: BorderRadius.circular(10),
              ),
              child: const Icon(Icons.arrow_back_ios_new, size: 18),
            ),
          ),
          const SizedBox(width: 20),
          const Text(
            "Mes Favoris",
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              fontFamily: 'Serif',
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildEmptyState() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.bookmark_border, size: 60, color: Colors.grey[400]),
          const SizedBox(height: 10),
          Text(
            "Aucun favori pour le moment",
            style: TextStyle(color: Colors.grey[600], fontSize: 16),
          ),
        ],
      ),
    );
  }

  Widget _buildFavoritesList() {
    return ListView.builder(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      itemCount: Favories.favoriteItems.length,
      itemBuilder: (context, index) {
        return _buildFavoriteCard(Favories.favoriteItems[index], index);
      },
    );
  }

  Widget _buildFavoriteCard(Map<String, String> item, int index) {
    return GestureDetector(
      onTap: () => Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => DetailPage(
            imagePath: item['image'] ?? "",
            title: item['title'] ?? "",
            description: item['description'] ?? "Aucune description disponible",
          ),
        ),
      ),
      child: Container(
        height: 200,
        margin: const EdgeInsets.only(bottom: 20),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.1),
              blurRadius: 10,
              offset: const Offset(0, 5),
            ),
          ],
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(20),
          child: Stack(
            children: [
              Positioned.fill(child: displayImage(item['image'])),
              Positioned.fill(
                child: Container(
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        Colors.transparent,
                        Colors.black.withOpacity(0.1),
                        Colors.black.withOpacity(0.8),
                      ],
                    ),
                  ),
                ),
              ),
              Positioned(
                bottom: 20,
                left: 20,
                right: 60,
                child: Text(
                  item['title']!,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    fontFamily: 'Serif',
                    shadows: [
                      Shadow(
                        color: Colors.black45,
                        blurRadius: 5,
                        offset: Offset(0, 2),
                      ),
                    ],
                  ),
                ),
              ),
              Positioned(
                top: 15,
                right: 15,
                child: GestureDetector(
                  onTap: () {
                    setState(() => Favories.favoriteItems.removeAt(index));
                    showMessage(context, "Favori supprimé");
                  },
                  child: Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.white.withOpacity(0.2),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(
                      Icons.delete,
                      color: Colors.orange,
                      size: 20,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
