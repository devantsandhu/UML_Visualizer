package src.test;

import java.security.Permission;

    // exception to be thrown by security manager when System.exit is called
    public class ExitException extends SecurityException {
        public final int status;

        public ExitException(int status) {
            this.status = status;
        }

        public int getStatus(){
            return this.status;
        }
}


// custom security manager
class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }
}