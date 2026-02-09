import 'package:flutter/material.dart';
import 'package:campus_events_app/pages/favories.dart';
import 'package:campus_events_app/utils.dart';

class DetailPage extends StatelessWidget {
  final String imagePath;
  final String title;
  final String description;

  const DetailPage({
    super.key,
    required this.imagePath,
    required this.title,
    required this.description,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFE8ECF4),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [_buildHeader(context), _buildContent()],
        ),
      ),
    );
  }

  Widget _buildHeader(BuildContext context) {
    return Stack(
      children: [
        ClipRRect(
          borderRadius: const BorderRadius.vertical(
            bottom: Radius.circular(30),
          ),
          child: Container(
            height: 320,
            width: double.infinity,
            foregroundDecoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.center,
                colors: [Colors.black.withOpacity(0.3), Colors.transparent],
              ),
            ),
            child: displayImage(imagePath),
          ),
        ),
        Positioned(
          top: 50,
          left: 20,
          right: 20,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              _buildBackButton(context),
              const Text(
                "Details",
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 18,
                  fontWeight: FontWeight.w600,
                ),
              ),
              _buildFavoriteButton(context),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildBackButton(BuildContext context) {
    return GestureDetector(
      onTap: () => Navigator.pop(context),
      child: Container(
        padding: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: Colors.white.withOpacity(0.3),
          borderRadius: BorderRadius.circular(15),
          border: Border.all(color: Colors.white.withOpacity(0.5)),
        ),
        child: const Icon(
          Icons.arrow_back_ios_new,
          color: Colors.white,
          size: 20,
        ),
      ),
    );
  }

  Widget _buildFavoriteButton(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Favories.favoriteItems.add({
          "image": imagePath,
          "title": title,
          "description": description,
        });
        showMessage(context, "$title ajouté aux favoris !");
      },
      child: Container(
        padding: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: const Color(0xFFFF7043),
          borderRadius: BorderRadius.circular(15),
          boxShadow: [
            BoxShadow(
              color: const Color(0xFFFF7043).withOpacity(0.4),
              blurRadius: 10,
              offset: const Offset(0, 5),
            ),
          ],
        ),
        child: const Icon(Icons.bookmark, color: Colors.white, size: 20),
      ),
    );
  }

  Widget _buildContent() {
    return Padding(
      padding: const EdgeInsets.all(25.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: const TextStyle(
              color: Colors.black,
              fontSize: 28,
              fontWeight: FontWeight.bold,
              fontFamily: 'Serif',
            ),
          ),
          const SizedBox(height: 30),
          const Text(
            "Description",
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
              color: Colors.black87,
            ),
          ),
          const SizedBox(height: 15),
          Text(
            description,
            style: TextStyle(
              color: Colors.grey[600],
              fontSize: 15,
              height: 1.6,
            ),
          ),
        ],
      ),
    );
  }
}
