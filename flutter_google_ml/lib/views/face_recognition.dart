import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class FaceRecognition extends StatefulWidget {
  FaceRecognition({Key key, this.channel}) : super(key: key);
  final MethodChannel channel;

  @override
  _FaceRecognitionState createState() => _FaceRecognitionState();
}

class _FaceRecognitionState extends State<FaceRecognition> {

  String _result = "";

  Future<void> getScannedFace() async {
    String retVal;
    try {
      final String result = await widget.channel.invokeMethod('getScannedFace');
      retVal = result;
    } on PlatformException catch (e) {
      retVal = e.message;
    }

    setState(() {
      print(retVal);
      _result = retVal;
    });
  }

  void _scan() {
    getScannedFace();
  }

  @override
  Widget build(BuildContext conFace) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Face Scan"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Face found:',
            ),
            Text(
              _result,
              style: Theme.of(conFace).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _scan,
        tooltip: 'Scan',
        child: Icon(Icons.search),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}