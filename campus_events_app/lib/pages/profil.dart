import 'package:campus_events_app/pages/bottomnav.dart';
import 'package:campus_events_app/pages/add_event.dart';
import 'package:campus_events_app/pages/inter.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:campus_events_app/utils.dart';

class Profil extends StatefulWidget {
  const Profil({super.key});

  @override
  State<Profil> createState() => _ProfilState();
}

class _ProfilState extends State<Profil> {
  // Récupération de l'utilisateur connecté
  final User? user = FirebaseAuth.instance.currentUser;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFE8ECF4),
      body: SafeArea(
        child: user == null
            ? const Center(child: Text("Non connecté"))
            : StreamBuilder<DocumentSnapshot>(
                stream: FirebaseFirestore.instance
                    .collection('users')
                    .doc(user!.uid)
                    .snapshots(),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(child: CircularProgressIndicator());
                  }

                  Map<String, dynamic>? userData;
                  if (snapshot.hasData && snapshot.data!.data() != null) {
                    userData = snapshot.data!.data() as Map<String, dynamic>;
                  }

                  String name =
                      userData?['name'] ?? user?.displayName ?? "Utilisateur";
                  String email =
                      userData?['email'] ?? user?.email ?? "Non connecté";
                  String imageUrl = userData?['image_url'] ?? "";

                  return Padding(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 20.0,
                      vertical: 10.0,
                    ),
                    child: Column(
                      children: [
                        _buildHeader(),
                        const SizedBox(height: 30),
                        const Text(
                          "Profil",
                          style: TextStyle(
                            fontSize: 28,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'Serif',
                            color: Colors.black,
                          ),
                        ),
                        const SizedBox(height: 30),
                        _buildProfileImage(
                          imageUrl.isNotEmpty ? imageUrl : user?.photoURL,
                        ),
                        const SizedBox(height: 15),
                        Text(
                          name,
                          style: const TextStyle(
                            fontSize: 22,
                            fontWeight: FontWeight.bold,
                            color: Colors.black87,
                          ),
                        ),
                        const SizedBox(height: 50),
                        _buildInfoRow(Icons.email, "E-Mail", email),
                        const Spacer(),
                        _buildButton(
                          text: "Ajouter un événement",
                          color: const Color(0xFF0F4E7F),
                          onPressed: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => const AddEventPage(),
                            ),
                          ),
                        ),
                        const SizedBox(height: 15),
                        _buildButton(
                          text: "Se déconnecter",
                          color: Colors.redAccent,
                          onPressed: () async {
                            await FirebaseAuth.instance.signOut();
                            if (context.mounted) {
                              Navigator.pushAndRemoveUntil(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => const WelcomePage(),
                                ),
                                (route) => false,
                              );
                            }
                          },
                        ),
                        const SizedBox(height: 20),
                      ],
                    ),
                  );
                },
              ),
      ),
    );
  }

  Widget _buildHeader() {
    return Stack(
      alignment: Alignment.center,
      children: [
        Align(
          alignment: Alignment.centerLeft,
          child: Container(
            decoration: BoxDecoration(
              border: Border.all(color: Colors.grey.shade300),
              borderRadius: BorderRadius.circular(12),
              color: Colors.transparent,
            ),
            child: IconButton(
              icon: const Icon(Icons.arrow_back_ios_new, size: 18),
              onPressed: () {
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (context) => const Bottomnav()),
                );
              },
            ),
          ),
        ),
        Image.asset("assets/images/logorond.jpg", height: 40),
      ],
    );
  }

  Widget _buildProfileImage(String? imageUrl) {
    return Container(
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        border: Border.all(color: Colors.white, width: 4),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 10,
            offset: const Offset(0, 5),
          ),
        ],
      ),
      child: CircleAvatar(
        radius: 60,
        backgroundColor: Colors.grey[300],
        child: ClipOval(child: displayImage(imageUrl, width: 120, height: 120)),
      ),
    );
  }

  Widget _buildInfoRow(IconData icon, String label, String value) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Icon(icon, size: 28, color: Colors.black87),
        const SizedBox(width: 20),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              label,
              style: const TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Colors.black87,
              ),
            ),
            const SizedBox(height: 5),
            Text(
              value,
              style: TextStyle(fontSize: 16, color: Colors.grey[800]),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildButton({
    required String text,
    required Color color,
    required VoidCallback onPressed,
  }) {
    return SizedBox(
      width: double.infinity,
      height: 55,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: color,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(15),
          ),
          elevation: 2,
        ),
        child: Text(
          text,
          style: const TextStyle(
            fontSize: 18,
            color: Colors.white,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
