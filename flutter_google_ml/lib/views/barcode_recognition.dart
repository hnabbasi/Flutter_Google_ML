import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class BarcodeRecognition extends StatefulWidget {
  BarcodeRecognition({Key key, this.channel}) : super(key: key);
  final MethodChannel channel;

  @override
  _BarcodeRecognitionState createState() => _BarcodeRecognitionState();
}

class _BarcodeRecognitionState extends State<BarcodeRecognition> {

  String _result = "";

  Future<void> getScannedBarcode() async {
    String retVal;
    try {
      final String result = await widget.channel.invokeMethod('getScannedBarcode');
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
    getScannedBarcode();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Barcode Scan"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Barcode found:',
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