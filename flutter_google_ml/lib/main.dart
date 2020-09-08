import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_google_ml/views/home.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  static const _channel = const MethodChannel('com.intelliabb.flutter_google_ml');
  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Google ML Kit Demo',
      theme: ThemeData(
        primarySwatch: Colors.purple,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(channel: _channel),
    );
  }
}