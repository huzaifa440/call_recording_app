// lib/main.dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'services/permission_service.dart';
import 'services/recording_service.dart';
import 'services/settings_service.dart';
import 'services/call_log_service.dart';
import 'screens/home_screen.dart';
import 'screens/settings_screen.dart';
import 'screens/call_log_screen.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => RecordingService()),
        ChangeNotifierProvider(create: (_) => SettingsService()),
        ChangeNotifierProvider(create: (_) => CallLogService()),
        Provider(create: (_) => PermissionService()),
      ],
      child: MaterialApp(
        title: 'Call Recording App',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        initialRoute: '/',
        routes: {
          '/': (context) => const HomeScreen(),
          '/settings': (context) => const SettingsScreen(),
          '/call-log': (context) => CallLogScreen(),
        },
      ),
    );
  }
}
