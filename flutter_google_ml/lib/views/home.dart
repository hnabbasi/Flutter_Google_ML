import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_google_ml/views/text_recognition.dart';

import 'barcode_recognition.dart';
import 'face_recognition.dart';
import 'object_recognition.dart';

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.channel}) : super(key: key);
  final MethodChannel channel;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Google ML Kit"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ListTile(
              contentPadding: EdgeInsets.fromLTRB(30, 15, 30, 15),
              leading: Icon(Icons.text_fields),
              title: Text("Text Recognition"),
              subtitle: Text("Scan text and recognize"),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => TextRecognition(channel: widget.channel))
                );
              },
            ),
            ListTile(
              contentPadding: EdgeInsets.fromLTRB(30, 15, 30, 15),
              leading: Icon(Icons.border_clear),
              title: Text("Barcode Recognition"),
              subtitle: Text("Scan barcode and recognize"),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => BarcodeRecognition(channel: widget.channel))
                );
              },
            ),
            ListTile(
              contentPadding: EdgeInsets.fromLTRB(30, 10, 30, 10),
              leading: Icon(Icons.face),
              title: Text("Face Recognition"),
              subtitle: Text("Scan face and recognize"),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => FaceRecognition(channel: widget.channel))
                );
              },
            ),
            ListTile(
              contentPadding: EdgeInsets.fromLTRB(30, 10, 30, 10),
              leading: Icon(Icons.beach_access),
              title: Text("Object Recognition"),
              subtitle: Text("Scan object and recognize"),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => ObjectRecognition(channel: widget.channel))
                );
              },
            ),      
          ],
        ),
      ),// This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}