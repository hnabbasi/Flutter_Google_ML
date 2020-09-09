import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class TextRecognition extends StatefulWidget {
  TextRecognition({Key key, this.channel}) : super(key: key);
  final MethodChannel channel;

  @override
  _TextRecognitionState createState() => _TextRecognitionState();
}

class _TextRecognitionState extends State<TextRecognition> {

  String _result = "";

  Future<void> getScannedText() async {
    String retVal;
    try {
      final String result = await widget.channel.invokeMethod('getScannedText');
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
    getScannedText();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Text Scan"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Icon(Icons.text_fields),
            SizedBox(height: 10),
            Text(
              'Scan text to recognize',
            ),
            Center(
              child: Text(
              _result,
              style: Theme.of(context).textTheme.headline4,
              ),
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