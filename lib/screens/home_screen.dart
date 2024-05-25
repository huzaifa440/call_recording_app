// lib/screens/home_screen.dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:call_recording_app/services/recording_service.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final recordings = Provider.of<RecordingService>(context).recordings;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Recorded Calls'),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              Navigator.pushNamed(context, '/settings');
            },
          ),
          IconButton(
            icon: const Icon(Icons.list),
            onPressed: () {
              Navigator.pushNamed(context, '/call-log');
            },
          ),
        ],
      ),
      body: ListView.builder(
        itemCount: recordings.length,
        itemBuilder: (context, index) {
          final recording = recordings[index];
          return ListTile(
            title: Text(recording.name),
            subtitle: Text(recording.date),
            onTap: () {
              // Play the recording
            },
            trailing: IconButton(
              icon: const Icon(Icons.delete),
              onPressed: () {
                Provider.of<RecordingService>(context, listen: false)
                    .deleteRecording(recording);
              },
            ),
          );
        },
      ),
    );
  }
}
