import 'dart:async';
import 'package:flutter/material.dart';
import 'package:campus_events_app/pages/inter.dart';

class First extends StatefulWidget {
  const First({super.key});

  @override
  State<First> createState() => _FirstState();
}

class _FirstState extends State<First> {
  @override
  void initState() {
    super.initState();
    // Lance un minuteur de 4 secondes avant de naviguer vers la page suivante
    Timer(const Duration(seconds: 4), () {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const WelcomePage()),
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(
        0xFFE8ECF4,
      ), // Même couleur que les autres pages
      body: Center(
        child: Image.asset(
          "assets/images/logorond.jpg",
          height: 150, // Taille ajustée pour être bien visible
        ),
      ),
    );
  }
}
