// lib/services/permission_service.dart
import 'package:permission_handler/permission_handler.dart';

class PermissionService {
  Future<bool> requestPermissions() async {
    final phonePermission = await Permission.phone.request();
    final storagePermission = await Permission.storage.request();

    return phonePermission.isGranted && storagePermission.isGranted;
  }
}
