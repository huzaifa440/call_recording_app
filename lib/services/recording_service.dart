// lib/services/recording_service.dart
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class RecordingService extends ChangeNotifier {
  static const platform =
      MethodChannel('com.example.call_recording_app/recording');

  List<Recording> _recordings = [];

  List<Recording> get recordings => _recordings;

  Future<void> startRecording(String fileName) async {
    try {
      await platform.invokeMethod('startRecording', {'fileName': fileName});
    } on PlatformException catch (e) {
      print("Failed to start recording: '${e.message}'.");
    }
  }

  Future<void> stopRecording() async {
    try {
      await platform.invokeMethod('stopRecording');
    } on PlatformException catch (e) {
      print("Failed to stop recording: '${e.message}'.");
    }
  }

  void addRecording(Recording recording) {
    _recordings.add(recording);
    notifyListeners();
  }

  void deleteRecording(Recording recording) {
    _recordings.remove(recording);
    notifyListeners();
  }
}

class Recording {
  final String name;
  final String date;

  Recording(this.name, this.date);
}
