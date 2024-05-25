// lib/screens/settings_screen.dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:call_recording_app/services/settings_service.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final settingsService = Provider.of<SettingsService>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            ListTile(
              title: const Text('Storage Location'),
              subtitle: Text(settingsService.storageLocation),
              onTap: () {
                // Implement storage location picker
              },
            ),
            SwitchListTile(
              title: const Text('Enable Call Recording'),
              value: settingsService.isRecordingEnabled,
              onChanged: (value) {
                settingsService.setRecordingEnabled(value);
              },
            ),
          ],
        ),
      ),
    );
  }
}
