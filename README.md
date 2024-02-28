# Lambda SnapStart Auto-Priming Guide

## Goal

This guide aims to explain techniques for priming Java applications without prior knowledge of their functionality. It assumes a base understanding of AWS Lambda, Lambda SnapStart, and CRaC.

## What is priming

Priming is the name given to the process of preparing a Java application for the process of being snapshotted. Priming aims to reduce the time of the first useful work after a restore. A short introduction is included in the AWS Compute blog [Reducing Java cold starts on AWS Lambda functions with SnapStart](https://aws.amazon.com/blogs/compute/reducing-java-cold-starts-on-aws-lambda-functions-with-snapstart/).

Although I will use Lambda and SnapStart examples, this would most likely be suitable for any use of CRaC.

