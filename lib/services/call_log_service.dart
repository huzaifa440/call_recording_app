import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
// import 'package:call_log/call_log.dart';

class CallLogService extends ChangeNotifier {
  static const platform =
      MethodChannel('com.example.call_recording_app/call_logs');

  final List<CallLog> _callLogs = [];

  List<CallLog> get callLogs => _callLogs;

  Future<void> fetchCallLogs() async {
    try {
      final List<dynamic> logs = await platform.invokeMethod('getCallLogs');
      _callLogs.clear();
      for (var log in logs) {
        _callLogs.add(CallLog(
          phoneNumber: log['phoneNumber'],
          contactName: log['contactName'],
          date: log['date'],
          type: log['type'],
        ));
      }
      notifyListeners();
    } on PlatformException catch (e) {
      print("Failed to get call logs: '${e.message}'.");
    }
  }

  void addCallLog(CallLog callLog) {
    _callLogs.add(callLog);
    notifyListeners();
  }

  void deleteCallLog(CallLog callLog) {
    _callLogs.remove(callLog);
    notifyListeners();
  }
}

class CallLog {
  final String phoneNumber;
  final String contactName;
  final String date;
  final String type; // incoming or outgoing

  CallLog({
    required this.phoneNumber,
    required this.contactName,
    required this.date,
    required this.type,
  });
}




// // lib/services/call_log_service.dart
// import 'package:flutter/material.dart';

// class CallLogService extends ChangeNotifier {
//   final List<CallLog> _callLogs = [];

//   List<CallLog> get callLogs => _callLogs;

//   void addCallLog(CallLog callLog) {
//     _callLogs.add(callLog);
//     notifyListeners();
//   }

//   void deleteCallLog(CallLog callLog) {
//     _callLogs.remove(callLog);
//     notifyListeners();
//   }
// }

// class CallLog {
//   final String phoneNumber;
//   final String contactName;
//   final String date;
//   final String type; // incoming or outgoing

//   CallLog({
//     required this.phoneNumber,
//     required this.contactName,
//     required this.date,
//     required this.type,
//   });
// }
