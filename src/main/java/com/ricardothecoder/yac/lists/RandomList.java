package com.ricardothecoder.yac.lists;

import java.util.ArrayList;
import java.util.Random;

public class RandomList<T> extends ArrayList<T>
{
	public void addWeighted(T value, int weight)
	{
		for (int i = 0; i < weight; i++)
			this.add(value);
	}
	
	public T getRandomValue()
	{
		return get(new Random().nextInt(this.size()));
	}
}
