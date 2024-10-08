# Call Graph Soundness in Android Static Analysis

This repository hosts the dataset and the artifacts used in our study.

:warning: :warning: :warning:
:loudspeaker: All artifacts are available in Zenodo, **<ins>LINK BELOW</ins>**.

## Link to the Zenodo archive

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.10400505.svg)](https://doi.org/10.5281/zenodo.10400505)

:link: https://doi.org/10.5281/zenodo.10400505

## Artifact Folder Structure

Below is a brief explanation of each directory of our artifacts:

- **dataset/**: Contains the dataset used in our study.
- **dynamic_analysis/**: Contains everything needed to reproduce our dynamic analysis experiments.
- **static_analysis/**: Contains everything needed to reproduce our static analyses experiments.
- **instrumentation/**: Contains the necessary files to instrument the apps for the dynamic analysis.
- **SLR/**: Contains the excel files with the papers collected during our Systematic Literature Review (SLR).

Please ensure that all the necessary files and resources are present in the respective directories before running any experiments.

## Installing the tool for static analysis

<pre>
mvn clean install
</pre>

### Using the tool

<pre>
java -jar Call-Graph-Soundness-in-Android-Static-Analysis/target/Experiments-1.0-jar-with-dependencies.jar <i>options</i>
</pre>

## License

This project is licensed under the GNU LESSER GENERAL PUBLIC LICENSE 2.1 - see the [LICENSE](LICENSE) file for details

## Contact

For any question regarding this study, please contact us at:
[Jordan Samhi](mailto:jordan.samhi@uni.lu)
