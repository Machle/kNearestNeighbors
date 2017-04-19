import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by tano on 29.12.16.
 */
public class Main {

    public static double euristicDistance(Individual ind1, Individual ind2){
        double distance = 0;

        for(int i=0;i<ind1.attr.length;i++){
            distance+=Math.pow(ind1.attr[i] - ind2.attr[i],2);
        }

        return Math.sqrt(distance);
    }

    public static ArrayList neighbors(int k, Individual testIndiv, ArrayList<Individual> trainSet){
        ArrayList<Double> distances = new ArrayList();
        HashMap<Double, Individual> dict = new HashMap();

        for(int i =0; i<trainSet.size();i++){
            double dist = euristicDistance(testIndiv,trainSet.get(i));
            distances.add(dist);
            dict.put(dist, trainSet.get(i));
        }
        Collections.sort(distances);

        ArrayList<Individual> neighbors = new ArrayList();
        for(int i=0;i<k;i++){
            neighbors.add(dict.get(distances.get(i)));
        }

        return neighbors;
    }
    public static String result(ArrayList<Individual> neighbors){
        int[] classVotes = new int[3];
        for(int i=0;i<neighbors.size();i++){
            if(neighbors.get(i).indiv_class.equals("Iris-setosa")){
                classVotes[0] += 1;
            } else if(neighbors.get(i).indiv_class.equals("Iris-versicolor")){
                classVotes[1] += 1;
            } else {
                classVotes[2] += 1;
            }
        }
        int index =0;
        int max =0;
        for(int i = 0;i<classVotes.length;i++){
            if(classVotes[i] > max){
                max = classVotes[i];
                index = i;
            }
        }

        if(index == 0)
            return "Iris-setosa";
        else if(index == 1)
            return "Iris-versicolor";
        else
            return "Iris-virginica";
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        InputStream fis = new FileInputStream("iris.data");
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        String line;
        ArrayList<Individual> indiv = new ArrayList<Individual>();
        while ((line = br.readLine()) != null) {
            String[] arr = line.split(",");
            Individual new_ind = new Individual();
            new_ind.indiv_class = arr[arr.length-1];
            for (int i = 0; i < arr.length-1; i++) {

                new_ind.attr[i] = Double.parseDouble(arr[i]);

            }
            indiv.add(new_ind);
        }

        Collections.shuffle(indiv);
        ArrayList<Individual> test = new ArrayList<>();
        ArrayList<Individual> train = new ArrayList<>();
        for(int i = 0; i<indiv.size();i++){
            if(i<20) {
                test.add(indiv.get(i));
            } else {
                train.add(indiv.get(i));
            }
        }

        Scanner in = new Scanner(System.in);
        System.out.print("Enter K: ");
        int k = in.nextInt();

        double correct = 0;
        for(int i =0;i<test.size();i++){
            ArrayList<Individual> neighbors = neighbors(k,test.get(i), train);
            String result = result(neighbors);
            if(result.equals(test.get(i).indiv_class)){
                correct++;
            }
            System.out.println("Prediction: " + result + " Expected:" + test.get(i).indiv_class);
        }
        System.out.println("Accuracy: " + (correct/(double) test.size())*100 + "%");
    }
}
