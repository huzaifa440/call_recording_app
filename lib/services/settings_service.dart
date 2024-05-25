// lib/services/settings_service.dart
import 'package:flutter/material.dart';

class SettingsService extends ChangeNotifier {
  String _storageLocation = '/storage/emulated/0/CallRecordings';
  bool _isRecordingEnabled = true;

  String get storageLocation => _storageLocation;

  bool get isRecordingEnabled => _isRecordingEnabled;

  void setStorageLocation(String location) {
    _storageLocation = location;
    notifyListeners();
  }

  void setRecordingEnabled(bool enabled) {
    _isRecordingEnabled = enabled;
    notifyListeners();
  }
}
