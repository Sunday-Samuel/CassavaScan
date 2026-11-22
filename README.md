# CassavaScan

![Platform](https://img.shields.io/badge/Platform-Android-blue)
![Model](https://img.shields.io/badge/Model-TensorFlow%20Lite-orange)
![Trained with](https://img.shields.io/badge/Trained%20with-Teachable%20Machine-brightgreen)
![Min Android](https://img.shields.io/badge/Android-8.0%2B-lightgrey)
![License](https://img.shields.io/badge/License-MIT-lightgrey)


CassavaScan is an Android app that checks cassava leaves for common diseases. It runs the model on the phone, so scanning works offline once the app is installed.

This repository contains:

1. The image model, trained with Google Teachable Machine
2. The conversion to a phone-ready TensorFlow Lite model
3. The native Android app, with camera and gallery scanning
4. A GitHub Actions workflow that builds the APK automatically
5. Versioned releases, each with its APK attached

---

## Pipeline

```text
Labelled cassava and non-leaf photos
      ↓
Google Teachable Machine (image model, 6 classes)
      ↓
TensorFlow Lite (model_unquant.tflite → cassava_model.tflite)
      ↓
Android app (built by GitHub Actions)
      ↓
APK, published under Releases
```

---

## Repository Structure

```bash
CassavaScan/
│
├── LICENSE
├── README.md
├── CHANGELOG.md
├── .gitignore
│
├── .github/
│   └── workflows/
│       ├── build-apk.yml       # Builds the APK on every push to main
│       └── release.yml         # Builds and attaches the APK when a version tag is pushed
│
├── android-app/                # Android project
│   ├── app/
│   │   └── src/main/
│   │       ├── assets/         # cassava_model.tflite (the deployed model)
│   │       ├── java/           # App code: scanning, classifier, history, info screens
│   │       └── res/            # Layouts, colours, text, icons
│   ├── build.gradle
│   ├── gradlew
│   └── settings.gradle
│
├── conversion/                 # Conversion and checking scripts
│   └── converted_keras.zip
    └── converted_tflite.zip
    └── tm-my-image-model.zip
│
└── docs/
    ├── screenshots/            # Screenshots of CassavaScan
    └── test_image/             # Sample leaf images for testing
```

---

## Model Overview

### Supported classes

* Cassava Bacterial Blight
* Cassava Brown Streak Disease
* Cassava Green Mottle
* Cassava Mosaic Disease
* Healthy Cassava Leaf
* Not a Cassava Leaf (rejected, with a "No cassava leaf found" message)

### Training data

* Kaggle cassava leaf disease competition photos
* Makerere University cassava image dataset (CC0)
* Openly licensed non-leaf photos from Pexels and Wikimedia Commons
* Field photos taken by the developer

Each class was balanced to the same number of training photos, using a 90/10 train/test split.

---

## Requirements

* **Phone:** Android 8.0 (API 26) or newer
* **Internet:** not needed for scanning
* **To build:** a GitHub account (the build runs in the cloud, so no Android Studio is needed)

---

## Installation

### Option 1: Install from Releases (recommended)

1. Go to the **Releases** page of this repository.
2. Download the latest `CassavaScan-vX.Y.Z.apk`.
3. Open it on your phone and allow installs from that source when asked.

### Option 2: Build from source

1. Open the **Actions** tab and select the newest **Build APK** run.
2. Download the **CassavaScan** artifact and unzip it.
3. Install `CassavaScan.apk` on your phone.

To build locally with Android Studio, open the `android-app` folder and run:

```bash
./gradlew assembleDebug
```

---



## Known Issues

* The "Not a Cassava Leaf" class has only been tested on a small set of everyday objects.
* Bacterial Blight and Green Mottle have fewer test photos than the other classes.
* Accuracy so far is measured on held-out photos from the same sources as the training data, so accuracy on real farms may be lower.
* Some on-screen text is still being reviewed before release.

---

## Releases

Each version is published under **Releases** with its APK attached. Version tags follow the form `v1.0.0`. See [CHANGELOG.md](CHANGELOG.md) for what changed in each version.

---

## Original Project

CassavaScan is based on the open-source CassavaCare project by Emmanuel Kirui Barkacha:

https://github.com/ekbarkacha/cassava-disease-mobile-deployment

The original project is released under the MIT licence. Its copyright notice and licence are kept in the `LICENSE` file and in the source file headers.

---

## Credits

* **Emmanuel Kirui Barkacha**, for the original CassavaCare project
* **Kaggle**, for the cassava leaf disease competition data
* **Makerere University**, for the cassava image dataset (CC0)
* **Google**, for Teachable Machine and TensorFlow Lite

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contact

**Sunday Samuel**
GitHub: [github.com/Sunday-Samuel](https://github.com/Sunday-Samuel)
