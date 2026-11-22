# CassavaScan

CassavaScan is an Android app that checks cassava leaves for common diseases. The model runs on the phone, so scanning works offline once the app is installed.

## What it does

- Scan a leaf with the camera, or choose a photo from the gallery
- Shows the likely disease and how confident the model is
- Gives simple advice for each result
- Keeps a history of scans on the phone
- Explains common cassava diseases, with farming tips

## Detects

- Cassava Bacterial Blight
- Cassava Brown Streak Disease
- Cassava Green Mottle
- Cassava Mosaic Disease
- Healthy leaves
- Photos that are not cassava leaves are rejected

## Limits

- Use photos of cassava leaves only, in daylight
- Results are a guide, not a confirmed diagnosis
- Accuracy so far is measured on held-out photos taken from the same sources as the training data, so accuracy on real farms may be lower

## Model

- Image model trained with Google Teachable Machine
- Training data: Kaggle cassava leaf disease competition photos, the Makerere University cassava image dataset (CC0), and other openly licensed photos
- Packaged inside the app as a TensorFlow Lite model

## Field testing

Field testing with extension officers is planned. Results will be added here once the officer has labelled the farm photos.

| Date | Location | Photos | Officer-labelled correct | Notes |
|---|---|---|---|---|
| Planned | Planned | 20 | Not yet tested | |

## Known issues

- The "Not a Cassava Leaf" class has only been tested on a small set of everyday objects
- Bacterial Blight and Green Mottle have fewer test photos than the other classes
- Some on-screen text is still being reviewed before release

## Requirements

- Android 8.0 (API 26) or newer
- No internet connection needed for scanning

## Build the app

The app builds automatically with GitHub Actions. Open the **Actions** tab, select the newest run, and download the **CassavaScan** artifact.

To build locally, open the `android-app` folder in Android Studio and run `./gradlew assembleDebug`.

## Releases

Each version is published under **Releases** with its APK attached. Version tags follow the form `v1.0.0`. See [CHANGELOG.md](CHANGELOG.md) for what changed in each version.

## Original project

CassavaScan is based on the open-source CassavaCare project by Emmanuel Kirui Barkacha:
https://github.com/ekbarkacha/cassava-disease-mobile-deployment

The original project is released under the MIT licence. Its copyright notice and licence are kept in the `LICENSE` file and in the source file headers.

## Credits

- Emmanuel Kirui Barkacha, for the original CassavaCare project
- Kaggle, for the cassava leaf disease competition data
- Makerere University, for the cassava image dataset (CC0)
- Google, for Teachable Machine and TensorFlow Lite

## Licence

MIT. See [LICENSE](LICENSE).

## Contact

Sunday Samuel: [github.com/Sunday-Samuel](https://github.com/Sunday-Samuel)
