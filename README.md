# Lambda SnapStart Auto-Priming Guide

## Goal

This guide aims to explain techniques for priming Java applications without prior knowledge of their functionality. It assumes a base understanding of AWS Lambda, Lambda SnapStart, and CRaC.

## What is Priming

Priming is the name given to the process of preparing a Java application for the process of being snapshotted. Priming aims to reduce the time of the first useful work after a restore. A short introduction is included in the AWS Compute blog [Reducing Java cold starts on AWS Lambda functions with SnapStart](https://aws.amazon.com/blogs/compute/reducing-java-cold-starts-on-aws-lambda-functions-with-snapstart/).

Although I will use Lambda and SnapStart examples, this would most likely be suitable for any use of CRaC.

## Manual Priming

Manual priming uses knowledge of the application functionality to prepare it for peak performance post-restore. This priming strategy uses the `org.crac.Resource` interface to execute the functionality of the Java application in the `beforeCheckpoint` method. Doing this means that those code paths are JIT compiled before the snapshot is taken. Common Lambda use cases include invoking non-mutating methods of @Controllers or other entry points to your application. This is the ideal case, as calling mutating methods can have awkward consequences in downstream systems, even if it's only done as part of a new deployment.

The following example creates a new Spring component that implements `org.crac.Resource`. It registers itself as part of the constructor. The class also uses constructor dependency injection to access the controller. In the `beforeCheckpoint` method it uses the controller to invoke a read operation on a repository. This means that Java loads all the classes in that path and JIT compiles them. This synthetic request is made before the snapshot is taken, outside of any customer request.

```Java
@Component
public class UnicornPrimingResource implements Resource {

    private static final Logger logger = LogManager.getLogger();

    private final UnicornController unicornController;

    public UnicornPrimingResource(UnicornController unicornController) {
        this.unicornController = unicornController;
        Core.getGlobalContext().register(this);
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) {
        try {
            unicornController.retrieveUnicorn("123");
        } catch (RuntimeException e) {
            // expected exception when unicorn doesn't exist.
        }
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) {
    }
}
```

Once the application is restored, future operations to this controller endpoint will benefit from not having to be compiled. Although the network connection will have to be recreated, the benefit of priming the application framework, AWS SDK / database driver, and Java networking stack can amount to hundreds of milliseconds. 


### Manually Priming Challenges 






