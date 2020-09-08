import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ObjectRecognition extends StatefulWidget {
  ObjectRecognition({Key key, this.channel}) : super(key: key);
  final MethodChannel channel;

  @override
  _ObjectRecognitionState createState() => _ObjectRecognitionState();
}

class _ObjectRecognitionState extends State<ObjectRecognition> {

  String _result = "";

  Future<void> getScannedObject() async {
    String retVal;
    try {
      final String result = await widget.channel.invokeMethod('getScannedObject');  
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
    getScannedObject();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Object Scan"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Object found:',
            ),
            Text(
              _result,
              style: Theme.of(context).textTheme.headline4,
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