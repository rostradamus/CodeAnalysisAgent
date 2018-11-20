package src.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * TODO: Following Class should transform the class that runs Main as followings:
 *  - Add a new global variable that stores: Set<ClassTracker> classTrackers
 *  - public void main(String args[]) {
 *      beforeAll {
 *          - instantiate: Set<ClassTracker> classTrackers
 *      }
 *
 *      ...
 *      original implementation
 *      ...
 *
 *      afterAll {
 *          - creates output file
 *          - in output file, writes: Set<ClassTracker> classTrackers' information, recursively
 *          - catch Exceptions (e.g. IOException)
 *          - finally close output file
 *      }
 *
 *  }
 */
public class MainClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return new byte[0];
    }
}
