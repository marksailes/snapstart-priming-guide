# Lambda SnapStart Priming Guide

## Goal

This guide aims to explain techniques for priming Java applications. It assumes a base understanding of AWS Lambda, Lambda SnapStart, and CRaC.

## Introduction to Lambda SnapStart

Lambda SnapStart is a performance optimization feature. It reduces the amount of time it takes to return a response to the caller when a new execution environment has been created (cold start).
At deployment time, it creates an execution environment and loads your code into it. Once the init phase has completed it takes a snapshot of the entire [FireCracker](https://firecracker-microvm.github.io/) microVM. This includes processes, memory, and any files on the file system. The snapshot is encrypted and placed in persistent storage. Then when future execution environments are required, they can be restored from the snapshot instead of being created from scratch. To further improve the restore time, snapshots are cached.

Read the rest at [my blog](https://sailes.co.uk/blog/lambda-snapstart-priming-guide/)
