package dudzinski.kacper.farec;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class ParseTree extends StackPane {

    private double currentY = 0;
    private double currentX = 0;

    public ParseTree(RegularExpression regularExpression){
        this.setAlignment(Pos.TOP_CENTER);
        buildTree(regularExpression);
    }

    private void buildTree(RegularExpression regularExpression){
        if (regularExpression instanceof SimpleRegularExpression){
            SimpleRegularExpression regex = (SimpleRegularExpression) regularExpression;
            StackPane leafNode = createNode(regex.getSymbol());
            leafNode.setTranslateX(currentX);
            leafNode.setTranslateY(currentY);
            this.getChildren().add(leafNode);
        }
        else if (regularExpression instanceof ComplexRegularExpression){
            ComplexRegularExpression regex = (ComplexRegularExpression) regularExpression;
            StackPane operatorNode = createNode(Parser.getOperatorChar(regex.getOperator()));
            operatorNode.setTranslateX(currentX);
            operatorNode.setTranslateY(currentY);
            this.getChildren().add(operatorNode);

            int currentDepth = regex.getDepth();
            double xChange = Math.pow(2,(double) currentDepth - 1) * 50;
            double yChange = 80;
            if (regex.getOperator() == Parser.REOperators.STAR){
                currentY += yChange;
                buildTree(regex.getLeftOperand());
                currentY -= yChange;
            }
            else{
                currentY += yChange;
                currentX -= xChange;
                buildTree(regex.getLeftOperand());
                currentX += xChange * 2;
                buildTree(regex.getRightOperand());
                currentX -= xChange;
                currentY -= yChange;
            }
        }
    }

    public StackPane createNode(char title){
        int radius = 20;
        StackPane nodePane = new StackPane();
        nodePane.setMaxSize(radius * 2, radius * 2);
        Circle node = new Circle();
        node.setRadius(radius);
        node.setStyle("-fx-fill: white;-fx-stroke: black;-fx-stroke-width:2px");
        Label nodeTitle = new Label("" + title);
        nodePane.getChildren().addAll(node, nodeTitle);
        return nodePane;
    }



}

