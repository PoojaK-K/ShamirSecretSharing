import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONTokener;

class Share {
    BigInteger x;
    BigInteger y;

    Share(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
}

public class ShamirSecretSharing {

    public static BigInteger lagrangeInterpolation(List<Share> shares) {
        BigInteger secret = BigInteger.ZERO;

        for (int i = 0; i < shares.size(); i++) {
            BigInteger xi = shares.get(i).x;
            BigInteger yi = shares.get(i).y;

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < shares.size(); j++) {
                if (i != j) {
                    BigInteger xj = shares.get(j).x;
                    numerator = numerator.multiply(xj.negate());
                    denominator = denominator.multiply(xi.subtract(xj));
                }
            }

            BigInteger term = yi.multiply(numerator).divide(denominator);
            secret = secret.add(term);
        }

        return secret;
    }

    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader("question1.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            JSONObject keys = jsonObject.getJSONObject("keys");
            int k = keys.getInt("k");

            List<Share> shares = new ArrayList<>();

            for (int i = 1; i <= k; i++) {
                JSONObject root = jsonObject.getJSONObject(String.valueOf(i));
                int base = Integer.parseInt(root.getString("base"));
                String value = root.getString("value");

                BigInteger x = new BigInteger(String.valueOf(i));
                BigInteger y = new BigInteger(value, base);

                shares.add(new Share(x, y));
            }

            BigInteger secret = lagrangeInterpolation(shares);

            System.out.println(secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
