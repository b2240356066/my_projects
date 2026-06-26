public class Exceptions extends Exception {
    public Exceptions(String message) {
        /**
         * the parent class exception is used.
         */
        super(message);
    }



    public static class AnimalNotFound extends Exceptions {
        /**
         * @param message The message that will be written
         */
        public AnimalNotFound(String message) {
            super(message);

        }
    }

    public static class UserNotFound extends Exceptions {
        /**
         * @param message The message that will be written
         */
        public UserNotFound(String message) {
            super(message);
        }
    }

    public static class CannotProcessCommand extends Exceptions {
        /**
         * @param message The message that will be written
         */
        public CannotProcessCommand(String message) {
            super(message);
        }
    }

    public static class NotEnoughPlant extends Exceptions {
        /**
         * @param message The message that will be written
         */
        public NotEnoughPlant(String message) {
            super(message);
        }
    }

    public static class NotEnoughFish extends Exceptions {
        /**
         * @param message The message that will be written
         */
        public NotEnoughFish(String message) {
            super(message);
        }
    }

    public static class NotEnoughMeat extends Exceptions {
        /**
         * @param message The message that will be written
         */
        public NotEnoughMeat(String message) {
            super(message);
        }
    }




}
