package Technical;

import lombok.Builder;
import lombok.Data;



    @Data
    @Builder(toBuilder = true)


    public class Cypher {

        /**de encoding method*/
        private static String applyCypherKey(String string) {
            char p = 'P';

            String outputString = "";
            int l = string.length();

            //apply key
            for (int i = 0; i < l; i++) {
                outputString = outputString + (char)(string.charAt(i)^p);
            }

            return outputString;
        }

        public static String encode(String string) {
            return applyCypherKey(string);
        }

        public static String decode(String string) {
            return applyCypherKey(string);
        }
    }



