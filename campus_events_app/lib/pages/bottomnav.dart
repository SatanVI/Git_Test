import 'package:flutter/material.dart';
import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:campus_events_app/pages/favories.dart';
import 'package:campus_events_app/pages/home.dart';
import 'package:campus_events_app/pages/profil.dart';

class Bottomnav extends StatefulWidget {
  const Bottomnav({super.key});

  @override
  State<Bottomnav> createState() => _BottomnavState();
}

class _BottomnavState extends State<Bottomnav> {
  int currentTabIndex = 0;

  // Simplification : On définit la liste des pages directement ici
  final List<Widget> pages = [const Home(), const Favories(), const Profil()];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      bottomNavigationBar: CurvedNavigationBar(
        index: currentTabIndex,
        height: 65,
        backgroundColor: const Color(0xFFE8ECF4), // Couleur de fond des pages
        color: const Color(0xFF0F4E7F), // Couleur de la barre (Bleu Ynov)
        animationDuration: const Duration(milliseconds: 500),
        onTap: (int index) => setState(() {
          currentTabIndex = index;
        }),
        items: const [
          Icon(Icons.home_outlined, color: Colors.white, size: 30),
          Icon(Icons.favorite_outline, color: Colors.white, size: 30),
          Icon(Icons.person_outline, color: Colors.white, size: 30),
        ],
      ),
      body: AnimatedSwitcher(
        duration: const Duration(milliseconds: 400),
        transitionBuilder: (Widget child, Animation<double> animation) {
          return FadeTransition(opacity: animation, child: child);
        },
        child: pages[currentTabIndex],
      ),
    );
  }
}
