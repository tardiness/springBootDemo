package com.test.other;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;



class TreeNode {
	int data;
	TreeNode leftNode;
	TreeNode rightNode;

	public TreeNode() {

	}

	public TreeNode(int d) {
		data = d;
	}

	public TreeNode(TreeNode left, TreeNode right, int d) {
		leftNode = left;
		rightNode = right;
		data = d;
	}

}
public class DeepFirstSort {



	public static void main(String[] args) {
		TreeNode head = new TreeNode(1);
		TreeNode second = new TreeNode(2);
		TreeNode three = new TreeNode(3);
		TreeNode four = new TreeNode(4);
		TreeNode five = new TreeNode(5);
		TreeNode six = new TreeNode(6);
		TreeNode seven = new TreeNode(7);
		head.rightNode = three;
		head.leftNode = second;
		second.rightNode = five;
		second.leftNode = four;
		three.rightNode = seven;
		three.leftNode = six;
		System.out.print("广度优先遍历结果：");
		new DeepFirstSort().BroadFirstSearch(head);
		System.out.println();
		System.out.print("深度优先遍历结果：");
		new DeepFirstSort().depthFirstSearch(head);
		System.out.println();
		System.out.print("深度优先遍历(递归)结果：");
		new DeepFirstSort().depthTraversal(head);
        System.out.println();
        System.out.print("前序结果：");
        new DeepFirstSort().beforeShow(head);
	}

	//广度优先遍历是使用队列实现的
	public void BroadFirstSearch(TreeNode nodeHead) {
		if (nodeHead == null) {
			return;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(nodeHead);
		while (!queue.isEmpty()) {
			nodeHead = queue.poll();
			System.out.print(nodeHead.data+" ");
			if (nodeHead.leftNode != null) {
				queue.offer(nodeHead.leftNode);
			}
			if (nodeHead.rightNode != null) {
				queue.offer(nodeHead.rightNode);
			}
		}
	}

	//深度优先遍历
	public void depthFirstSearch(TreeNode nodeHead) {
		if (nodeHead == null) {
			return;
		}
		Stack<TreeNode> nodes = new Stack<>();
		nodes.push(nodeHead);
		TreeNode treeNode = null;
		while (!nodes.isEmpty()) {
			treeNode = nodes.pop();
			System.out.print(treeNode.data+" ");
			if (treeNode.rightNode != null) {
				nodes.push(treeNode.rightNode);
			}
			if (treeNode.leftNode != null) {
				nodes.push(treeNode.leftNode);
			}
		}
	}


	// 递归 深度优先遍历

	private void depthTraversal(TreeNode nodeHead)
	{
		if (nodeHead != null) {
			System.out.print(nodeHead.data + " ");
			depthTraversal(nodeHead.leftNode);
			depthTraversal(nodeHead.rightNode);
		}
	}

	private void beforeShow(TreeNode root) {
		if (root != null) {
			System.out.print(root.data+" ");
			beforeShow(root.leftNode);
			beforeShow(root.rightNode);
		}
	}

}