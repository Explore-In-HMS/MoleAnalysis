<br />
<p align="center">
  <h2 align="center">Mole Analysis</h2>
  <p align="center">
    Mole Analysis Use Case for HMS ML Kit Custom Model
                   
  ![Open Source Love svg3](https://badges.frapsoft.com/os/v3/open-source.svg?v=103)
  ![hms-2022](https://img.shields.io/badge/Project-2022-1f425f.svg?color=red)
  ![mindsporeLite](https://img.shields.io/badge/Mindspore-Lite-1f425f.svg)
  ![hms-mlkit](https://img.shields.io/badge/HMS%20-MLKit-1f425f.svg?color=darkgreen)
  ![mlkit-custom](https://img.shields.io/badge/MLKit-Custom%20Model-1f425f.svg?color=green)
  ![image-class](https://img.shields.io/badge/Image%20Classification-HMS%20AI%20Create-1f425f.svg?color=brightgreen)
  
 

<br>

  
  ![Kotlin](https://img.shields.io/badge/language-kotlin-blue)
  ![Minimum SDK Version](https://img.shields.io/badge/minSDK-22-orange) 
  ![Android Gradle Version](https://img.shields.io/badge/androidGradleVersion-7.0.2-green) 
  ![Gradle Version](https://img.shields.io/badge/gradleVersion-7.0.2-informational)



<hr>
                   
  <img src="https://github.com/Explore-In-HMS/MoleAnalysis/blob/main/images/MoleAnalysis.gif?raw=true"  />

<p float="left">
  <img src="https://github.com/Explore-In-HMS/MoleAnalysis/blob/main/images/left-mole.png" width="400" />
   <img src="https://github.com/Explore-In-HMS/MoleAnalysis/blob/main/images/right-mole.png" width="400" />
</p>

</p>

</table>
                                                                                                     



# Introduction

### What is Melanoma?

Melanoma is the most serious among skin cancers because it can spread to various parts of the body. Its incidence has also increased in our country and it has fatal results.The key to the treatment of this serious melanoma is early diagnosis. Providing this is simple: The person should detect the changes by self-controlling the moles in his body.Therefore, it is important for a person to be aware of the changes in the moles in his body and to follow them.
We trained our model on cancerous me-cells using Huawei ML Kit Custom Model Generation with our Mole Analysis application, which will help you in your diagnosis and follow-up.

### Sign of Melanoma?

<br><img align="left" src="https://github.com/Explore-In-HMS/MoleAnalysis/blob/main/images/sign.png?raw=true" width="500"> </p>

Factors that may increase your risk of melanoma include: Fair skin, A history of sunburn, Excessive ultraviolet (UV) light exposure, Living closer to the equator or at a higher elevation, Having many moles or unusual moles, A family history of melanoma, Weakened immune system.
<br><br>
If you have one of the risk factors for melanoma, experts recommend doing a skin self-exam. One of the most common methods for detecting melanomas is the ABCDE method. Developed by doctors to help patients easily remember the symptoms of melanoma, it details the warning signs in moles that often indicate cancer. Monthly skin checks are important as most melanomas start as a new mole or skin growth. <br><br>

<br clear="left"/>




### How will the Mole Analysis app help me ?
Mole Analysis application is trained with Huawei ML Kit model with collected mole data. Although there is an accuracy rate of 89% according to Train and Test data, <ins>but also of course there is a possibility of error.</ins> The machine learning trained Mole Analysis app will give you an idea to detect your changes and get a preliminary idea by comparing it with the data it is trained on. <br>

You can get an idea of the symptoms of melanoma with the Mole Analysis app, <ins>but it's NOT a definitive diagnosis.</ins>  We aim to provide early diagnosis and early diagnosis despite its potential risk. <ins>**We recommend that you CONSULT A DOCTOR about the results for a definitive diagnosis.**</ins>


# About Huawei ML Kit

ML Kit allows your apps to easily leverage Huawei's long-term proven expertise in machine learning to support diverse artificial intelligence (AI) applications throughout a wide range of industries. Thanks to Huawei's technology accumulation, ML Kit provides diversified leading machine learning capabilities that are easy to use, helping you develop various AI apps. [More..](https://developer.huawei.com/consumer/en/hms/huawei-mlkit/)

### Mindspore Lite
As an on-device inference framework of the custom model, the on-device inference framework MindSpore Lite provided by ML Kit facilitates integration and development and can be running on devices

#### Advantages 
- It provides simple and complete APIs for you to integrate the inference framework of an on-device custom model. In this way, you can customize the model in the simplest and quickest way, providing you with excellent experience of machine learning.
- It is compatible with all mainstream model inference platforms or frameworks, such as MindSpore Lite, TensorFlow Lite, Caffe, and Onnx in the market. Different models can be converted into the .ms format without any loss, and then run perfectly through the on-device inference framework.
- Custom models occupy small storage space and can be quantized and compressed. Models can be quickly deployed and executed. In addition, models can be hosted on the cloud and downloaded as required, reducing the APK size.
- The kernel code of the inference framework is open-source for global developers. It gathers the wisdom of global contributors to improve and continuously iterate itself. It not only brings you a friendly development ecosystem, but also is your mentor on the path of machine learning inference.


# Model Preparation

### Used Dataset
[ISIC Melanoma Research Datasets](https://challenge.isic-archive.com/data/) <br>
[The HAM10000 dataset](https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/DBW86T)

### Preparation

1. Two labels were identified: <br>
- **Benign**: Non-cancerous 
- **Malignant**: Suspected of Melonama 

2. Incomprehensible data in the dataset has been cleared. (important to increase the success of the model, but challenge for the mole dataset)

3. Data is foldered by labels

4. The dataset is split into Train: 80%, Test: 20%. At the same time, a close number of data was separated according to the labels.

 - Train Dataset: 8k Benign, 8k Malignant image files
 - Test Dataset: 1.5k Benign, 1.5k Malignant image files

# Getting Started

## Development Enviroment

-	JDK version: 1.8.211 or later
- Android Studio version: 3.X or later
-	minSdkVersion: 19 or later (mandatory)
-	targetSdkVersion: 30 (recommended)
-	compileSdkVersion: 30 (recommended)
-	Gradle version: 4.6 or later (recommended)
- Test device: a Huawei phone running EMUI 5.0 or later, or a non-Huawei phone running 
  
## How To Start

-	Register a developer account on HUAWEI Developers and configure.
- Register in to [Huawei Developer Console](https://developer.huawei.com/consumer/en/console) and Create and configure an app
-	To use ML  Kit, you need to enable it in AppGallery Connect. For details, please refer to [Enabling Services](https://developer.huawei.com/consumer/en/doc/distribution/app/agc-help-enabling-service-0000001146598793).
- Adding the AppGallery Connect Configuration File of Your App
    	- Sign in to AppGallery Connect and click My projects.
      - Find your project and click the app for which you want to integrate the HMS Core SDK.
      - On the Project Setting page, set SHA-256 certificate fingerprint to the SHA-256 fingerprint you've generated. 
      - Go to Project settings > General information. In the App information area, download the agconnect-services.json file.
-	Configuring the Maven Repository Address for the HMS Core SDK
-	Open the build.gradle file in the root directory of your Android Studio project.
-	Add the AppGallery Connect plugin and the Maven repository.


```sh
buildscript { 
    repositories { 
        google() 
        jcenter() 
        // Configure the Maven repository address for the HMS Core SDK. 
        maven {url 'https://developer.huawei.com/repo/'} 
    } 
    dependencies { 
        ... 
        // Add the AppGallery Connect plugin configuration. You are advised to use the latest plugin version. 
        classpath 'com.huawei.agconnect:agcp:1.6.0.300' 
    } 
} 
 
allprojects { 
    repositories { 
        google() 
        jcenter() 
        // Configure the Maven repository address for the HMS Core SDK. 
        maven {url 'https://developer.huawei.com/repo/'} 
    } 
} 

```


Adding Build Dependencies (app build gradle)
```sh

 implementation 'com.huawei.hms:base:6.4.0.302'
    //HMS Custom Model
    implementation 'com.huawei.hms:ml-computer-model-executor:3.5.0.301'
    //MindsporeLite
    implementation 'mindspore:mindspore-lite:5.0.5.300'

```

Permissions ( AndroidManifest.xml )

```sh

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


```

Important adding this aaptOptions for your model .ms file
```sh
aaptOptions {
        noCompress "ms", "mnn", "cambricon"
        cruncherEnabled false
        useNewCruncher false
    }
```

## ML Kit Model Training

### Preparing the Environment
 ![mindsporeLite](https://img.shields.io/badge/Python-V3.7.5-1f425f.svg)
1. Install the HMS Tool Kit plugin in Android Studio. [Installation](https://developer.huawei.com/consumer/en/doc/development/Tools-Guides/installation-0000001050145206)
2. HMS Toolkit > Coding Assistant > AI > AI Create
3. HMS Toolkit automatically downloads resources.
4- Python version should be **3.7.5** and Python environment variable to path.
5- The MindSpore tool is automatically installed. If the installation fails, the following dialog box is displayed, asking you whether to install it manually.
    - Open the command line tool to manually install MindSpore.
```sh
run pip install MindSpore installation file 
```

### Model Training 

1. In Coding Assistant, go to AI > AI Create > Image.
2. Drag or add the image classification folders to the Please select train image folder area, then set Output model file path and train parameters.
3. Advanced 
    - Iteration count: The default value is 100.
    - Learning rate: The default value is 0.01.
4. Click Create Model to start training and generate an image classification model. 
  Then you will see results:


  Learning Results:
  <br><img src="https://github.com/Explore-In-HMS/MoleAnalysis/blob/main/images/train_acc.jpg?raw=true" width="400"> 

5. After the model training is complete, you can verify the model by adding the image folders in the Add test image area
<br><img src="https://github.com/Explore-In-HMS/MoleAnalysis/blob/main/images/result.jpg?raw=true" width="400"> 


# Challenges
As with any machine learning project, the challenges and aspects of this project that need improvement:
- Mole data is very difficult to distinguish
- The process of cleaning the data is challenging
- Analysis of no moles when irrelevant data is loaded
- Importance of camera angle

# References

The resources used in the development of the project are as follows:

* [HMS ML Kit](https://developer.huawei.com/consumer/en/hms/huawei-mlkit/)
* [Mindspore Lite](https://www.mindspore.cn/lite/en)
* [HMS Toolkit ](https://developer.huawei.com/consumer/en/huawei-toolkit/)


[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub visitors](https://img.shields.io/badge/dynamic/json?color=red&label=visitors&query=value&url=https%3A%2F%2Fapi.countapi.xyz%2Fhit%2FExplore-In-HMS.Mole-Analysis%2Freadme)<br>
Copyright © 2022 <br />
