// lib/screens/call_log_screen.dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:call_recording_app/services/call_log_service.dart';
// import 'package:call_log/call_log.dart';

class CallLogScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final callLogs = Provider.of<CallLogService>(context).callLogs;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Call Log'),
      ),
      body: ListView.builder(
        itemCount: callLogs.length,
        itemBuilder: (context, index) {
          final callLog = callLogs[index];
          return ListTile(
            title: Text(callLog.contactName.isNotEmpty
                ? callLog.contactName
                : callLog.phoneNumber),
            subtitle: Text('${callLog.date} (${callLog.type})'),
            onTap: () {
              // Show call details or recording if available
            },
          );
        },
      ),
    );
  }
}
